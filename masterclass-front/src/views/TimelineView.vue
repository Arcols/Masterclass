<script setup lang="ts">
import { ref, computed } from 'vue'
import Header from '@/components/Header.vue'
import EventCard, { type EventData } from '@/components/event/EventCard.vue'
import EventDetailModal from '@/components/modals/EventDetailModal.vue'
import { getTodoList } from '@/services/timelineService'
import { onMounted } from 'vue'
import { useAuth } from '@/utils/checkingAuth'
import FilterModal from '@/components/modals/FilterModal.vue'
import { FunnelIcon } from '@heroicons/vue/24/outline'
import { useFilters } from '@/composables/useFilters'
import GlobalFilterAlert from '@/components/GlobalFilterAlert.vue'

const { requireAuth } = useAuth()

const localEvents = ref<EventData[]>([])
const isLoading = ref(true)
const loadError = ref<string | null>(null)

onMounted(async () => {
  await requireAuth() // redirige vers /login si token invalide

  try {
    localEvents.value = await getTodoList()
  } catch (err) {
    loadError.value = 'Impossible de charger les événements.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
})

// Récupération des filtres de la session
const { selectedTypes, selectedGroups, showFavoritesOnly, resetFilters } = useFilters()

// Gestion de l'état de la modale de filtre
const isFilterModalOpen = ref(false)

// Calcul du nombre de filtres actifs
const activeFilterCount = computed(() => {
  return (
    selectedTypes.value.length + selectedGroups.value.length + (showFavoritesOnly.value ? 1 : 0)
  )
})

// Calcul des groupes disponibles pour les passer à la modale
const availableGroups = computed(() => {
  const groups = localEvents.value.map((e) => e.group)
  return [...new Set(groups)]
})

const upcomingTasks = computed(() => {
  return (
    localEvents.value
      // Règle 1 : timeline des devoirs/examens
      .filter((e) => e.type === 'devoir' || e.type === 'examen')
      // Règle 2 : Application des filtres globaux de la session
      .filter((e) => {
        const isTypeMatched =
          selectedTypes.value.length === 0 || selectedTypes.value.includes(e.type)
        const isGroupMatched =
          selectedGroups.value.length === 0 || selectedGroups.value.includes(e.group)
        const isFavoriteMatched = !showFavoritesOnly.value || e.isFavorite

        return isTypeMatched && isGroupMatched && isFavoriteMatched
      })
      .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
  )
})

// État pour la modale de détails
const selectedEvent = ref<EventData | null>(null)

const handleUpdateStatus = (id: string, newValue: boolean) => {
  const task = localEvents.value.find((e) => e.id === id)
  if (task) task.isCompleted = newValue
}

const handleDelete = (id: string) => {
  localEvents.value = localEvents.value.filter((e) => e.id !== id)
  selectedEvent.value = null
}
</script>

<template>
  <div class="w-full h-screen flex flex-col bg-[var(--color-background)] overflow-hidden relative">
    <Header
      class="relative z-[999] bg-[var(--color-background)] shadow-sm shrink-0"
      :show-add-event-button="false"
      :show-profile="true"
      :show-navigation="true"
      subtitle="FIL A1 2028"
    />

    <!-- bandeau d'alerte global -->
    <GlobalFilterAlert />

    <main class="flex-1 overflow-y-auto p-4 md:p-6 lg:p-8">
      <div class="max-w-3xl mx-auto w-full">
        <div class="flex items-start md:items-center justify-between gap-2 mb-5 md:mb-6">
          <div class="flex flex-col md:flex-row md:items-center gap-1 md:gap-3">
            <h1 class="text-lg md:text-2xl font-bold text-[var(--color-black)] leading-tight">
              Timeline Devoirs/Examens
            </h1>
            <span
              class="bg-[var(--color-primary)] text-white text-[9px] md:text-[10px] font-bold px-2 py-0.5 md:px-3 md:py-1 rounded-full uppercase tracking-widest shadow-sm w-fit"
            >
              Prochainement
            </span>
          </div>

          <div class="flex items-center gap-1.5 shrink-0">
            <button
              @click="isFilterModalOpen = true"
              class="relative flex items-center justify-center gap-2 p-2 md:px-3 md:py-1.5 bg-[var(--color-primary)] text-white rounded-md text-sm font-medium hover:opacity-90 transition-opacity cursor-pointer"
            >
              <FunnelIcon class="w-4 h-4 md:w-4 md:h-4" />
              <span class="hidden md:inline">Filtres</span>

              <span
                v-if="activeFilterCount > 0"
                class="absolute -top-2 -right-2 flex items-center justify-center min-w-[20px] h-5 px-1 text-[11px] font-bold text-white bg-[var(--color-red)] rounded-full border-2 border-white shadow-sm"
              >
                {{ activeFilterCount }}
              </span>
            </button>

            <button
              v-if="activeFilterCount > 0"
              @click="resetFilters"
              class="flex items-center justify-center p-2 md:p-1.5 border-2 border-[var(--color-primary)] text-[var(--color-primary)] rounded-md hover:bg-[var(--color-primary)]/10 transition-colors cursor-pointer"
              title="Effacer les filtres"
            >
              <svg
                class="w-4 h-4"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                stroke-width="2"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"
                />
                <path stroke-linecap="round" stroke-linejoin="round" d="M3 3l18 18" />
              </svg>
            </button>
          </div>
        </div>

        <div class="flex flex-col gap-3">
          <EventCard
            v-for="task in upcomingTasks"
            :key="task.id"
            :event="task"
            layout="list"
            @open-details="selectedEvent = task"
            @toggle-complete="handleUpdateStatus"
          />

          <div
            v-if="upcomingTasks.length === 0"
            class="text-center text-gray-500 py-12 bg-white rounded-xl border border-gray-100"
          >
            Aucun devoir ou examen à venir. Profites-en pour te reposer !
          </div>
        </div>
      </div>
    </main>

    <EventDetailModal
      v-if="selectedEvent"
      :event="selectedEvent"
      @close="selectedEvent = null"
      @toggle-complete="(id, val) => handleUpdateStatus(id, val)"
      @delete="handleDelete"
    />

    <FilterModal
      v-if="isFilterModalOpen"
      :selected-types="selectedTypes"
      :selected-groups="selectedGroups"
      :available-groups="availableGroups"
      :show-favorites-only="showFavoritesOnly"
      @update:selected-types="selectedTypes = $event"
      @update:selected-groups="selectedGroups = $event"
      @update:show-favorites-only="showFavoritesOnly = $event"
      @close="isFilterModalOpen = false"
    />
  </div>
</template>
