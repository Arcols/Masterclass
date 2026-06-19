<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import {
  XMarkIcon,
  CalendarIcon,
  ClockIcon,
  UserGroupIcon,
  MapPinIcon,
  TrashIcon,
  PencilIcon,
  CheckCircleIcon,
  CheckIcon,
  LinkIcon,
  PlusIcon,
  UserIcon,
  StarIcon as StarOutline,
} from '@heroicons/vue/24/outline'
import { StarIcon as StarSolid } from '@heroicons/vue/24/solid'
import EventBadge from '../event/EventBadge.vue'
import { useEventDetails } from '@/composables/useEventDetails'
import CommentItem from '@/components/event/CommentItem.vue'
import { useAuthToken } from '@/composables/useAuthToken'
import type { EventData } from '@/components/event/EventCard.vue'

const props = defineProps<{
  eventId: string
}>()

const emit = defineEmits<{
  (e: 'close'): void
  (e: 'delete', id: string): void
  (e: 'edit', event: EventData): void
  (e: 'toggle-complete', id: string, newValue: boolean): void
  (e: 'toggle-favorite', id: string, newValue: boolean): void
}>()

// ── FERMETURE AVEC LA TOUCHE ÉCHAP ──
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape') {
    emit('close')
  }
}

// ── APPEL API ET DONNÉES RÉACTIVES ──
const { fetchEventDetails, addComment, addNote, toggleEventCompletion, isLoading } = useEventDetails()
const { getUserIdFromToken } = useAuthToken()

const fullEvent = ref<any>(null)
const localComments = ref<any[]>([])
const localNotes = ref<any[]>([])
const currentUserId = ref<string | null>(null)

const formatBackendDate = (isoString: string) => {
  if (!isoString) return ''
  const date = new Date(isoString)
  const day = String(date.getDate()).padStart(2, '0')
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const year = date.getFullYear()
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${day}/${month}/${year} à ${hours}:${minutes}`
}

onMounted(async () => {
  document.addEventListener('keydown', handleKeydown)

  currentUserId.value = getUserIdFromToken()

  const data = await fetchEventDetails(props.eventId)

  if (data) {
    // On peuple fullEvent avec toutes les données de l'API
    fullEvent.value = {
      id: data.id,
      type: data.type ? data.type.toLowerCase() : 'activite',
      title: data.title,
      subject: data.subjectName,
      date: data.date,
      // Formatage des heures (le backend renvoie souvent "HH:MM:SS", on garde "HH:MM")
      startTime: data.startTime ? data.startTime.substring(0, 5) : '',
      endTime: data.endTime ? data.endTime.substring(0, 5) : '',
      description: data.description,
      location: data.location,
      submissionLink: data.submissionLink,
      group: data.groupName,
      creator: data.creator,
      isCompleted: data.completed,
      isFavorite: false
    }

    localComments.value = data.comments ? data.comments.map((c: any) => ({
      id: c.id,
      eventId: data.id,
      author: c.authorName,
      datetime: formatBackendDate(c.date),
      content: c.content
    })) : []

    localNotes.value = data.personalNotes ? data.personalNotes.map((n: any) => ({
      id: n.id,
      eventId: data.id,
      author: 'Moi',
      datetime: formatBackendDate(n.date),
      content: n.content
    })) : []
  }
})

// FONCTION POUR GÉRER LE CLIC SUR LE BOUTON
const handleToggleComplete = async () => {
  if (!currentUserId.value) return

  const newStatus = await toggleEventCompletion(props.eventId, currentUserId.value)

  if (newStatus !== null) {
    // On met à jour l'interface locale de la modale
    fullEvent.value.isCompleted = newStatus

    // On prévient le composant Parent (PlanningBoard) pour qu'il mette à jour
    // la petite carte sans avoir besoin de recharger toute la page !
    emit('toggle-complete', props.eventId, newStatus)
  }
}

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
})

const isDevoir = computed(() => fullEvent.value?.type === 'devoir')
const isExamen = computed(() => fullEvent.value?.type === 'examen')
const isRedDate = computed(() => isDevoir.value || isExamen.value)

// VÉRIFIER LE CRÉATEUR
const isCreator = computed(() => {
  return currentUserId.value && fullEvent.value?.creator?.id === currentUserId.value
})

// ── FORMATAGE DE LA DATE ──
const formattedDate = computed(() => {
  if (!fullEvent.value?.date) return 'Date non définie'

  const d = new Date(`${fullEvent.value.date}T00:00:00`)
  const options: Intl.DateTimeFormatOptions = { weekday: 'long', day: 'numeric', month: 'long' }
  const dateStr = new Intl.DateTimeFormat('fr-FR', options).format(d)

  if (isDevoir.value) {
    const timeStr = fullEvent.value.endTime ? fullEvent.value.endTime.replace(':', 'h') : ''
    return `A rendre pour le ${dateStr} à ${timeStr}`
  }

  const capitalizedDate = dateStr.charAt(0).toUpperCase() + dateStr.slice(1)
  return `${capitalizedDate} / ${fullEvent.value.startTime} - ${fullEvent.value.endTime}`
})

const modalTitle = computed(() => {
  if (isDevoir.value) return 'Détails du devoir'
  if (isExamen.value) return "Détails de l'examen"
  if (fullEvent.value?.type === 'sport') return "Détails de l'évènement sportif"
  return "Détails de l'activité"
})

// ── GESTION DES ONGLETS ──
const activeTab = ref<'commentaires' | 'notes'>('commentaires')

// Variables pour le formulaire d'ajout
const isAdding = ref(false)
const newItemContent = ref('')

// Fonction pour changer d'onglet (réinitialise l'état d'ajout)
const switchTab = (tab: 'commentaires' | 'notes') => {
  activeTab.value = tab
  isAdding.value = false
  newItemContent.value = ''
}

const currentEventComments = computed(() => localComments.value)
const currentEventNotes = computed(() => localNotes.value)

const submitNewItem = async () => {
  const content = newItemContent.value.trim()
  if (!content || !currentUserId.value) return

  const isComment = activeTab.value === 'commentaires'

  try {
    let savedItem;

    // On appelle le backend via notre composable
    if (isComment) {
      savedItem = await addComment(props.eventId, currentUserId.value, content)
    } else {
      savedItem = await addNote(props.eventId, currentUserId.value, content)
    }

    // On formate le retour de l'API pour notre interface
    const formattedItem = {
      id: savedItem.id,
      eventId: props.eventId,
      author: isComment ? savedItem.authorName : 'Moi',
      datetime: formatBackendDate(savedItem.date),
      content: savedItem.content
    }

    // On l'ajoute visuellement à la liste locale
    if (isComment) {
      localComments.value.push(formattedItem)
    } else {
      localNotes.value.push(formattedItem)
    }

    // On réinitialise l'interface
    newItemContent.value = ''
    isAdding.value = false
  } catch (error) {
    alert("Une erreur est survenue lors de l'envoi.")
  }
}
</script>

<template>
  <div
    class="fixed inset-0 z-999 flex items-center justify-center bg-[var(--color-black)]/40 backdrop-blur-sm p-4"
    @click.self="emit('close')"
  >
    <dialog
      open
      class="bg-white rounded-xl shadow-xl w-full max-w-lg max-h-[90vh] flex flex-col animate-fade-in-up p-0 m-0 border-none relative overflow-hidden"
      aria-labelledby="event-detail-title"
    >
      <div v-if="isLoading || !fullEvent" class="flex flex-col items-center justify-center p-12">
        <div class="w-10 h-10 border-4 border-[var(--color-primary)] border-t-transparent rounded-full animate-spin"></div>
        <p class="mt-4 text-sm text-gray-500 font-medium animate-pulse">Chargement des détails...</p>
      </div>

      <template v-else>
        <div class="shrink-0 flex items-center justify-between px-4 py-3 md:px-6 md:py-4 border-b border-gray-100">
          <h2 id="event-detail-title" class="text-base md:text-lg font-bold text-[var(--color-black)] truncate pr-2">
            {{ modalTitle }}
          </h2>

          <div class="flex items-center gap-1 shrink-0">
            <button
              @click="emit('toggle-favorite', fullEvent.id, !fullEvent.isFavorite)"
              class="p-1.5 rounded-full hover:bg-gray-100 transition-colors cursor-pointer"
              :title="fullEvent.isFavorite ? 'Retirer des favoris' : 'Ajouter aux favoris'"
            >
              <StarSolid v-if="fullEvent.isFavorite" class="w-5 h-5 md:w-6 md:h-6 text-[var(--color-event-favorite-selected)] drop-shadow-sm" />
              <StarOutline v-else class="w-5 h-5 md:w-6 md:h-6 text-[var(--color-event-favorite-unselected)] hover:text-gray-600" />
            </button>

            <button
              @click="emit('close')"
              class="p-1.5 rounded-full text-gray-400 hover:bg-gray-100 hover:text-gray-700 transition-colors cursor-pointer"
            >
              <XMarkIcon class="w-5 h-5 md:w-6 md:h-6" />
            </button>
          </div>
        </div>

        <div class="flex-1 overflow-y-auto p-4 md:p-6 space-y-4 md:space-y-6">
          <div class="flex items-center gap-3 md:gap-4">
            <div
              v-if="isDevoir || isExamen"
              class="w-10 h-10 md:w-12 md:h-12 shrink-0 rounded-full border border-gray-200 flex items-center justify-center bg-gray-50"
            >
              <CheckCircleIcon class="w-6 h-6 md:w-7 md:h-7 text-gray-500" />
            </div>
            <div
              v-else
              class="w-10 h-10 md:w-12 md:h-12 shrink-0 rounded-lg flex items-center justify-center bg-[var(--color-primary)]/10 text-[var(--color-primary)]"
            >
              <CalendarIcon class="w-6 h-6 md:w-7 md:h-7" />
            </div>

            <div class="flex flex-col items-start gap-0.5 md:gap-1">
              <EventBadge :type="fullEvent.type" />
              <h3 class="text-lg md:text-xl font-bold text-[var(--color-black)] leading-tight">{{ fullEvent.title }}</h3>
              <span v-if="fullEvent.subject" class="text-xs md:text-sm font-semibold text-gray-500">
                {{ fullEvent.subject }}
              </span>
            </div>
          </div>

          <div v-if="fullEvent.creator" class="flex items-center text-xs md:text-sm text-gray-800">
            <UserIcon class="w-4 h-4 md:w-5 md:h-5 mr-2 md:mr-3 shrink-0 text-gray-500" />
            Publié par : <span class="font-medium ml-1">{{ fullEvent.creator.name }}</span>
          </div>

          <hr class="border-gray-100" />

          <a
            v-if="fullEvent.submissionLink"
            :href="fullEvent.submissionLink"
            target="_blank"
            class="flex items-center gap-2 text-xs md:text-sm font-medium text-[var(--color-primary)] hover:underline bg-[var(--color-primary)]/5 p-2.5 md:p-3 rounded-lg border border-[var(--color-primary)]/20 transition-colors"
          >
            <LinkIcon class="w-4 h-4 shrink-0" />
            Accéder au lien du rendu
          </a>

          <div class="flex flex-col gap-2 md:gap-3">
            <div
              class="flex items-center text-xs md:text-sm"
              :class="isRedDate ? 'text-[var(--color-red)] font-medium' : 'text-gray-800'"
            >
              <ClockIcon class="w-4 h-4 md:w-5 md:h-5 mr-2 md:mr-3 shrink-0" />
              {{ formattedDate }}
            </div>
            <div class="flex items-center text-xs md:text-sm text-gray-800">
              <UserGroupIcon class="w-4 h-4 md:w-5 md:h-5 mr-2 md:mr-3 shrink-0 text-gray-500" />
              Groupe : <span class="font-medium ml-1">{{ fullEvent.group }}</span>
            </div>
            <div v-if="fullEvent.location" class="flex items-center text-xs md:text-sm text-gray-800">
              <MapPinIcon class="w-4 h-4 md:w-5 md:h-5 mr-2 md:mr-3 shrink-0 text-gray-500" />
              {{ fullEvent.location }}
            </div>
          </div>

          <div class="bg-gray-50 rounded-lg p-3 md:p-4 text-xs md:text-sm text-gray-600 border border-gray-100 whitespace-pre-wrap">
            {{ fullEvent.description || 'Aucune description pour cet événement.' }}
          </div>

          <div class="mt-6 md:mt-8 bg-gray-50/50 border border-gray-100 rounded-xl overflow-hidden">
            <div class="flex p-1 bg-gray-100/80 rounded-t-xl">
              <button
                @click="switchTab('commentaires')"
                class="flex-1 py-1.5 text-xs md:text-sm font-medium rounded-md transition-all cursor-pointer"
                :class="
                  activeTab === 'commentaires'
                    ? 'bg-white text-[var(--color-primary)] shadow-sm'
                    : 'text-gray-500 hover:text-gray-700'
                "
              >
                Commentaires
              </button>
              <button
                @click="switchTab('notes')"
                class="flex-1 py-1.5 text-xs md:text-sm font-medium rounded-md transition-all cursor-pointer"
                :class="
                  activeTab === 'notes'
                    ? 'bg-white text-[var(--color-primary)] shadow-sm'
                    : 'text-gray-500 hover:text-gray-700'
                "
              >
                Notes perso.
              </button>
            </div>

            <div class="p-3 md:p-4 bg-gray-50">
              <div v-if="activeTab === 'commentaires'">
                <p
                  v-if="currentEventComments.length === 0"
                  class="text-xs text-gray-400 mb-4 text-center italic"
                >
                  Aucun commentaire pour le moment.
                </p>
                <CommentItem
                  v-for="comment in currentEventComments"
                  :key="comment.id"
                  :author="comment.author"
                  :datetime="comment.datetime"
                  :content="comment.content"
                />
              </div>

              <div v-else>
                <p
                  v-if="currentEventNotes.length === 0"
                  class="text-xs text-gray-400 mb-4 text-center italic"
                >
                  Aucune note personnelle pour le moment.
                </p>
                <CommentItem
                  v-for="note in currentEventNotes"
                  :key="note.id"
                  :author="note.author"
                  :datetime="note.datetime"
                  :content="note.content"
                  is-note
                />
              </div>

              <div
                v-if="isAdding"
                class="mt-2 bg-white p-2 md:p-3 rounded-lg border border-gray-200 shadow-sm animate-fade-in-up"
              >
                <textarea
                  v-model="newItemContent"
                  rows="3"
                  class="w-full text-xs md:text-sm border border-gray-300 rounded-md focus:ring-2 focus:ring-[var(--color-primary)] focus:border-transparent resize-none p-2 outline-none transition-all"
                  :placeholder="
                    activeTab === 'commentaires'
                      ? 'Saisissez votre commentaire...'
                      : 'Saisissez votre note personnelle...'
                  "
                  autofocus
                ></textarea>
                <div class="flex justify-end gap-2 mt-2">
                  <button
                    @click="isAdding = false; newItemContent = ''"
                    class="px-3 py-1.5 text-xs font-medium text-gray-600 hover:bg-gray-100 rounded-md transition-colors cursor-pointer"
                  >
                    Annuler
                  </button>
                  <button
                    @click="submitNewItem"
                    class="px-3 py-1.5 text-xs font-medium bg-[var(--color-primary)] text-white rounded-md hover:opacity-90 transition-opacity cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                    :disabled="!newItemContent.trim()"
                  >
                    Publier
                  </button>
                </div>
              </div>

              <button
                v-else
                @click="isAdding = true"
                class="mt-2 flex items-center justify-center gap-2 w-full py-2 md:py-2.5 rounded-lg border-2 border-dashed border-gray-300 text-xs md:text-sm font-medium text-gray-500 hover:text-[var(--color-primary)] hover:border-[var(--color-primary)] hover:bg-[var(--color-primary)]/5 transition-all cursor-pointer"
              >
                <PlusIcon class="w-4 h-4" />
                {{ activeTab === 'commentaires' ? 'Ajouter un commentaire' : 'Ajouter une note' }}
              </button>
            </div>
          </div>
        </div>

        <div
          class="shrink-0 flex flex-col sm:flex-row items-stretch sm:items-center gap-2 sm:gap-3 p-3 md:p-4 border-t border-gray-100 bg-white"
        >
          <button
            v-if="isDevoir"
            @click="handleToggleComplete"
            class="w-full sm:w-auto flex-1 flex items-center justify-center gap-2 px-3 py-2 md:px-4 rounded-md font-medium text-xs md:text-sm transition-colors cursor-pointer"
            :class="
              fullEvent.isCompleted
                ? 'bg-gray-200 text-gray-700'
                : 'bg-[var(--color-primary)] text-white hover:opacity-90'
            "
          >
            <CheckIcon v-if="!fullEvent.isCompleted" class="w-4 h-4" />
            {{ fullEvent.isCompleted ? 'Marquer non fait' : 'Marquer comme fait' }}
          </button>

          <button
            v-if="isCreator"
            @click="emit('edit', fullEvent)"
            class="w-full sm:w-auto flex-1 flex items-center justify-center gap-2 px-3 py-2 md:px-4 rounded-md border border-gray-200 font-medium text-xs md:text-sm text-gray-700 hover:bg-gray-50 transition-colors cursor-pointer"
          >
            <PencilIcon class="w-4 h-4" />
            Modifier
          </button>

          <button
            v-if="isCreator"
            @click="emit('delete', fullEvent.id)"
            class="w-full sm:w-auto flex-1 flex items-center justify-center gap-2 px-3 py-2 md:px-4 rounded-md font-medium text-xs md:text-sm text-[var(--color-red)] bg-red-50 hover:bg-red-100 transition-colors cursor-pointer"
          >
            <TrashIcon class="w-4 h-4" />
            Supprimer
          </button>
        </div>
      </template>
    </dialog>
  </div>
</template>

<style scoped>
.animate-fade-in-up {
  animation: fadeInUp 0.2s ease-out forwards;
}
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px) scale(0.98);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}
</style>
