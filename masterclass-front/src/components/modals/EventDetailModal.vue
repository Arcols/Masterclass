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
  StarIcon as StarOutline,
} from '@heroicons/vue/24/outline'
import { StarIcon as StarSolid } from '@heroicons/vue/24/solid'
import EventBadge from '../event/EventBadge.vue'
import type { EventData } from '../event/EventCard.vue'

// Imports des mocks
import allComments from '@/mocks/comments.json'
import allNotes from '@/mocks/notes.json'
import CommentItem from '@/components/event/CommentItem.vue'

const props = defineProps<{
  event: EventData
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

onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
})

const isDevoir = computed(() => props.event.type === 'devoir')
const isExamen = computed(() => props.event.type === 'examen')
const isRedDate = computed(() => isDevoir.value || isExamen.value)

// ── FORMATAGE DE LA DATE ──
const formattedDate = computed(() => {
  if (!props.event.date) return 'Date non définie'

  const d = new Date(`${props.event.date}T00:00:00`)
  const options: Intl.DateTimeFormatOptions = { weekday: 'long', day: 'numeric', month: 'long' }
  const dateStr = new Intl.DateTimeFormat('fr-FR', options).format(d)

  if (isDevoir.value) {
    const timeStr = props.event.endTime.replace(':', 'h')
    return `A rendre pour le ${dateStr} à ${timeStr}`
  }

  const capitalizedDate = dateStr.charAt(0).toUpperCase() + dateStr.slice(1)
  return `${capitalizedDate} / ${props.event.startTime} - ${props.event.endTime}`
})

const modalTitle = computed(() => {
  if (isDevoir.value) return 'Détails du devoir'
  if (isExamen.value) return "Détails de l'examen"
  if (props.event.type === 'sport') return "Détails de l'évènement sportif"
  return "Détails de l'activité"
})

// ── GESTION DES ONGLETS & DONNÉES RÉACTIVES ──
const activeTab = ref<'commentaires' | 'notes'>('commentaires')

// On rend les données importées réactives pour pouvoir y ajouter de nouveaux éléments
const localComments = ref([...allComments])
const localNotes = ref([...allNotes])

// Variables pour le formulaire d'ajout
const isAdding = ref(false)
const newItemContent = ref('')

// Fonction pour changer d'onglet (réinitialise l'état d'ajout)
const switchTab = (tab: 'commentaires' | 'notes') => {
  activeTab.value = tab
  isAdding.value = false
  newItemContent.value = ''
}

// Filtrage dynamique lié à l'événement actuel
const currentEventComments = computed(() => {
  return localComments.value.filter((comment) => comment.eventId === props.event.id)
})

const currentEventNotes = computed(() => {
  return localNotes.value.filter((note) => note.eventId === props.event.id)
})

// Fonction d'ajout d'un commentaire ou d'une note
const submitNewItem = () => {
  if (!newItemContent.value.trim()) return

  // Création de la date formatée "JJ/MM/AAAA à HH:mm"
  const now = new Date()
  const day = String(now.getDate()).padStart(2, '0')
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const year = now.getFullYear()
  const hours = String(now.getHours()).padStart(2, '0')
  const minutes = String(now.getMinutes()).padStart(2, '0')
  const datetime = `${day}/${month}/${year} à ${hours}:${minutes}`

  if (activeTab.value === 'commentaires') {
    localComments.value.push({
      id: `c_${Date.now()}`, // ID généré temporairement
      eventId: props.event.id,
      author: 'Jean Dupont', // Utilisateur mocké par défaut
      datetime,
      content: newItemContent.value.trim(),
    })
  } else {
    localNotes.value.push({
      id: `n_${Date.now()}`,
      eventId: props.event.id,
      author: 'Jean Dupont',
      datetime,
      content: newItemContent.value.trim(),
    })
  }

  // Réinitialisation après l'ajout
  newItemContent.value = ''
  isAdding.value = false
}
</script>

<template>
  <div
    class="fixed inset-0 z-999 flex items-center justify-center bg-[var(--color-black)]/40 backdrop-blur-sm p-4"
    @click.self="emit('close')"
  >
    <dialog
      open
      class="bg-white rounded-xl shadow-xl w-full max-w-lg max-h-[90vh] flex flex-col animate-fade-in-up p-0 m-0 border-none relative"
      aria-labelledby="event-detail-title"
    >
      <div class="shrink-0 flex items-center justify-between px-6 py-4 border-b border-gray-100">
        <h2 id="event-detail-title" class="text-lg font-bold text-[var(--color-black)]">
          {{ modalTitle }}
        </h2>

        <!-- ── ACTIONS D'EN-TÊTE ── -->
        <div class="flex items-center gap-1">
          <!-- Bouton Favori -->
          <button
            @click="emit('toggle-favorite', event.id, !event.isFavorite)"
            class="p-1.5 rounded-full hover:bg-gray-100 transition-colors cursor-pointer"
            :title="event.isFavorite ? 'Retirer des favoris' : 'Ajouter aux favoris'"
          >
            <StarSolid v-if="event.isFavorite" class="w-6 h-6 text-[var(--color-event-favorite-selected)] drop-shadow-sm" />
            <StarOutline v-else class="w-6 h-6 text-[var(--color-event-favorite-unselected)] hover:text-gray-600" />
          </button>

          <!-- Bouton Fermer -->
          <button
            @click="emit('close')"
            class="p-1.5 rounded-full text-gray-400 hover:bg-gray-100 hover:text-gray-700 transition-colors cursor-pointer"
          >
            <XMarkIcon class="w-6 h-6" />
          </button>
        </div>
      </div>

      <div class="flex-1 overflow-y-auto p-6 space-y-6">
        <div class="flex items-center gap-4">
          <div
            v-if="isDevoir || isExamen"
            class="w-12 h-12 shrink-0 rounded-full border border-gray-200 flex items-center justify-center bg-gray-50"
          >
            <CheckCircleIcon class="w-7 h-7 text-gray-500" />
          </div>
          <div
            v-else
            class="w-12 h-12 shrink-0 rounded-lg flex items-center justify-center bg-[var(--color-primary)]/10 text-[var(--color-primary)]"
          >
            <CalendarIcon class="w-7 h-7" />
          </div>

          <div class="flex flex-col items-start gap-1">
            <EventBadge :type="event.type" />
            <h3 class="text-xl font-bold text-[var(--color-black)]">{{ event.title }}</h3>
            <span v-if="event.subject" class="text-sm font-semibold text-gray-500">
              {{ event.subject }}
            </span>
          </div>
        </div>

        <hr class="border-gray-100" />

        <div class="flex flex-col gap-3">
          <div
            class="flex items-center text-sm"
            :class="isRedDate ? 'text-[var(--color-red)] font-medium' : 'text-gray-800'"
          >
            <ClockIcon class="w-5 h-5 mr-3 shrink-0" />
            {{ formattedDate }}
          </div>
          <div class="flex items-center text-sm text-gray-800">
            <UserGroupIcon class="w-5 h-5 mr-3 shrink-0 text-gray-500" />
            Groupe : <span class="font-medium ml-1">{{ event.group }}</span>
          </div>
          <div v-if="event.location" class="flex items-center text-sm text-gray-800">
            <MapPinIcon class="w-5 h-5 mr-3 shrink-0 text-gray-500" />
            {{ event.location }}
          </div>
        </div>

        <a
          v-if="event.submissionLink"
          :href="event.submissionLink"
          target="_blank"
          class="flex items-center gap-2 text-sm font-medium text-[var(--color-primary)] hover:underline bg-[var(--color-primary)]/5 p-3 rounded-lg border border-[var(--color-primary)]/20 transition-colors"
        >
          <LinkIcon class="w-4 h-4 shrink-0" />
          Accéder au lien du rendu
        </a>

        <div class="bg-gray-50 rounded-lg p-4 text-sm text-gray-600 border border-gray-100">
          {{ event.description || 'Aucune description pour cet événement.' }}
        </div>

        <div class="mt-8 bg-gray-50/50 border border-gray-100 rounded-xl overflow-hidden">
          <div class="flex p-1 bg-gray-100/80 rounded-t-xl">
            <button
              @click="switchTab('commentaires')"
              class="flex-1 py-1.5 text-sm font-medium rounded-md transition-all cursor-pointer"
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
              class="flex-1 py-1.5 text-sm font-medium rounded-md transition-all cursor-pointer"
              :class="
                activeTab === 'notes'
                  ? 'bg-white text-[var(--color-primary)] shadow-sm'
                  : 'text-gray-500 hover:text-gray-700'
              "
            >
              Notes personnelles
            </button>
          </div>

          <div class="p-4 bg-gray-50">
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
              class="mt-2 bg-white p-3 rounded-lg border border-gray-200 shadow-sm animate-fade-in-up"
            >
              <textarea
                v-model="newItemContent"
                rows="3"
                class="w-full text-sm border border-gray-300 rounded-md focus:ring-2 focus:ring-[var(--color-primary)] focus:border-transparent resize-none p-2 outline-none transition-all"
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
              class="mt-2 flex items-center justify-center gap-2 w-full py-2.5 rounded-lg border-2 border-dashed border-gray-300 text-sm font-medium text-gray-500 hover:text-[var(--color-primary)] hover:border-[var(--color-primary)] hover:bg-[var(--color-primary)]/5 transition-all cursor-pointer"
            >
              <PlusIcon class="w-4 h-4" />
              {{ activeTab === 'commentaires' ? 'Ajouter un commentaire' : 'Ajouter une note' }}
            </button>
          </div>
        </div>
      </div>

      <div
        class="shrink-0 flex flex-col sm:flex-row items-stretch sm:items-center gap-2 sm:gap-3 p-4 border-t border-gray-100 bg-white"
      >
        <button
          v-if="isDevoir"
          @click="emit('toggle-complete', event.id, !event.isCompleted)"
          class="w-full sm:w-auto flex-1 flex items-center justify-center gap-2 px-4 py-2 rounded-md font-medium text-sm transition-colors cursor-pointer"
          :class="
            event.isCompleted
              ? 'bg-gray-200 text-gray-700'
              : 'bg-[var(--color-primary)] text-white hover:opacity-90'
          "
        >
          <CheckIcon v-if="!event.isCompleted" class="w-4 h-4" />
          {{ event.isCompleted ? 'Marqué non fait' : 'Marqué comme fait' }}
        </button>

        <button
          @click="emit('edit', event)"
          class="w-full sm:w-auto flex-1 flex items-center justify-center gap-2 px-4 py-2 rounded-md border border-gray-200 font-medium text-sm text-gray-700 hover:bg-gray-50 transition-colors cursor-pointer"
        >
          <PencilIcon class="w-4 h-4" />
          Modifier
        </button>

        <button
          @click="emit('delete', event.id)"
          class="w-full sm:w-auto flex-1 flex items-center justify-center gap-2 px-4 py-2 rounded-md font-medium text-sm text-[var(--color-red)] bg-red-50 hover:bg-red-100 transition-colors cursor-pointer"
        >
          <TrashIcon class="w-4 h-4" />
          Supprimer
        </button>
      </div>
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
