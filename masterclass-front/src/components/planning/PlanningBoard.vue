<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ChevronLeftIcon, ChevronRightIcon, FunnelIcon } from '@heroicons/vue/24/outline'
import PlanningColumn from './PlanningColumn.vue'
import type { EventData } from '@/components/event/EventCard.vue'
import FilterModal from '@/components/modals/FilterModal.vue'
import { useFilters } from '@/composables/useFilters'
import { useAuthToken } from '@/composables/useAuthToken'
import { getEventsByUserAndDates } from '@/services/eventService'

const { getUserIdFromToken } = useAuthToken()

const loadEvents = async () => {
  const userId = getUserIdFromToken()

  if (!userId) {
    console.warn('Aucun utilisateur connecté pour charger le planning')
    return
  }

  const startDate = weekDays.value[0].fullDateString
  const endDate = weekDays.value[6].fullDateString

  try {
    const rawEvents = await getEventsByUserAndDates(userId, startDate, endDate)

    events.value = rawEvents.map((e: any) => ({
      id: e.eveId,
      title: e.eveTitle,
      date: e.eveDate,
      startTime: e.eveStarthour,
      endTime: e.eveEndhour,
      type: e.eveType,
      location: e.eveLocation,
      group: e.subject?.subName || 'Général',
      isCompleted: false,
      isFavorite: false,
    }))
  } catch (error) {
    console.error('Erreur de récupération du planning :', error)
  }
}

// ── CONFIGURATION DE LA GRILLE ──
const START_HOUR = 7
const END_HOUR = 24
const MOBILE_ROW_HEIGHT = 50
const DESKTOP_ROW_HEIGHT = 80

const blocksCount = END_HOUR - START_HOUR
const hours = Array.from({ length: blocksCount + 1 }, (_, i) => START_HOUR + i)

const events = ref<EventData[]>([])
const rowHeight = ref(MOBILE_ROW_HEIGHT)

const updateRowHeight = () => {
  rowHeight.value = window.innerWidth < 768 ? MOBILE_ROW_HEIGHT : DESKTOP_ROW_HEIGHT
}

// ── GESTION DES FILTRES ──
// On récupère l'état global et persistant
const { selectedTypes, selectedGroups, showFavoritesOnly, resetFilters } = useFilters()

const isFilterModalOpen = ref(false)

// Calcul du nombre de filtres actifs
const activeFilterCount = computed(() => {
  return (
    selectedTypes.value.length + selectedGroups.value.length + (showFavoritesOnly.value ? 1 : 0)
  )
})

// Génère dynamiquement la liste de tous les groupes existants dans les données
const availableGroups = computed(() => {
  const groups = events.value.map((e) => e.group)
  return [...new Set(groups)] // Enlève les doublons
})

// ── FILTRAGE DES DONNÉES ──
const getEventsForDay = (fullDateStr: string) => {
  return events.value.filter((e) => {
    const isSameDate = e.date === fullDateStr
    const isTypeMatched = selectedTypes.value.length === 0 || selectedTypes.value.includes(e.type)
    const isGroupMatched =
      selectedGroups.value.length === 0 || selectedGroups.value.includes(e.group)
    const isFavoriteMatched = !showFavoritesOnly.value || e.isFavorite

    return isSameDate && isTypeMatched && isGroupMatched && isFavoriteMatched
  })
}

// ── GESTION DES DATES ──
const currentDate = ref(new Date())
const now = ref(new Date())
let timer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  updateRowHeight()
  window.addEventListener('resize', updateRowHeight)

  loadEvents()

  timer = setInterval(() => {
    now.value = new Date()
  }, 60000)
})

onUnmounted(() => {
  window.removeEventListener('resize', updateRowHeight)
  if (timer) clearInterval(timer)
})

const weekDays = computed(() => {
  const current = new Date(currentDate.value)
  const dayIndex = current.getDay()

  const distanceToMonday = dayIndex === 0 ? -6 : 1 - dayIndex

  const monday = new Date(current)
  monday.setDate(current.getDate() + distanceToMonday)

  const dayNames = [
    { short: 'Lun', full: 'Lundi' },
    { short: 'Mar', full: 'Mardi' },
    { short: 'Mer', full: 'Mercredi' },
    { short: 'Jeu', full: 'Jeudi' },
    { short: 'Ven', full: 'Vendredi' },
    { short: 'Sam', full: 'Samedi' },
    { short: 'Dim', full: 'Dimanche' },
  ]

  return dayNames.map((dayName, index) => {
    const nextDay = new Date(monday)
    nextDay.setDate(monday.getDate() + index)

    const yyyy = nextDay.getFullYear()
    const mm = String(nextDay.getMonth() + 1).padStart(2, '0')
    const dd = String(nextDay.getDate()).padStart(2, '0')
    const fullDateString = `${yyyy}-${mm}-${dd}`

    const isToday =
      nextDay.getDate() === now.value.getDate() &&
      nextDay.getMonth() === now.value.getMonth() &&
      nextDay.getFullYear() === now.value.getFullYear()

    return {
      id: `day-${index}`,
      name: dayName.short,
      fullName: dayName.full,
      dateNumber: nextDay.getDate(),
      fullDateString: fullDateString,
      isToday: isToday,
    }
  })
})

const currentMonthYear = computed(() => {
  return currentDate.value.toLocaleDateString('fr-FR', { month: 'long', year: 'numeric' })
})

const prevWeek = () => {
  const newDate = new Date(currentDate.value)
  newDate.setDate(newDate.getDate() - 7)
  currentDate.value = newDate
}

const nextWeek = () => {
  const newDate = new Date(currentDate.value)
  newDate.setDate(newDate.getDate() + 7)
  currentDate.value = newDate
}

const updateStatus = (id: string, newValue: boolean) => {
  const targetEvent = events.value.find((e) => e.id === id)
  if (targetEvent) targetEvent.isCompleted = newValue
}

const emit = defineEmits<{
  (e: 'open-details', event: EventData): void
  (e: 'request-add', payload: { date: string; startTime: string }): void
  (e: 'toggle-sidebar'): void
}>()

const props = defineProps<{
  isSidebarOpen?: boolean
}>()
</script>

<template>
  <div
    class="flex flex-col h-full bg-white md:rounded-xl border border-gray-200 overflow-hidden shadow-sm"
  >
    <div
      class="flex items-center justify-between px-4 py-3 border-b border-gray-200 bg-white z-50 shrink-0"
    >
      <h2 class="text-lg font-bold text-[var(--color-black)] capitalize hidden sm:block">
        {{ currentMonthYear }}
      </h2>

      <!-- Affichage simplifié sur mobile si besoin -->
      <h2 class="text-base font-bold text-[var(--color-black)] capitalize sm:hidden">
        {{ currentDate.toLocaleDateString('fr-FR', { month: 'short' }) }}
      </h2>

      <!-- ZONE DES BOUTONS D'ACTION (Filtres + Navigation) -->
      <div class="flex items-center gap-2 sm:gap-4">
        <!-- Bloc Filtres -->
        <div class="flex items-center gap-1.5">
          <button
            @click="isFilterModalOpen = true"
            class="relative flex items-center gap-2 px-3 py-1.5 bg-[var(--color-primary)] text-white rounded-md text-sm font-medium hover:opacity-90 transition-opacity cursor-pointer"
          >
            <FunnelIcon class="w-4 h-4" />
            <span class="hidden md:inline">Filtres</span>

            <!-- Badge Notification -->
            <span
              v-if="activeFilterCount > 0"
              class="absolute -top-2 -right-2 flex items-center justify-center min-w-[20px] h-5 px-1 text-[11px] font-bold text-white bg-[var(--color-red)] rounded-full border-2 border-white shadow-sm"
            >
              {{ activeFilterCount }}
            </span>
          </button>

          <!-- Bouton Reset Rapide (Visible uniquement si des filtres sont actifs) -->
          <button
            v-if="activeFilterCount > 0"
            @click="resetFilters"
            class="flex items-center justify-center p-1.5 border-2 border-[var(--color-primary)] text-[var(--color-primary)] rounded-md hover:bg-[var(--color-primary)]/10 transition-colors cursor-pointer"
            title="Effacer les filtres"
          >
            <!-- SVG personnalisé "Entonnoir Barré" -->
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

        <div class="w-px h-6 bg-gray-200 hidden sm:block"></div>

        <!-- Bloc Navigation Semaines & Sidebar -->
        <div class="flex items-center gap-1">
          <button
            @click="prevWeek"
            class="p-1.5 rounded-full hover:bg-gray-100 transition-colors cursor-pointer text-gray-600"
          >
            <ChevronLeftIcon class="w-5 h-5" />
          </button>
          <button
            @click="nextWeek"
            class="p-1.5 rounded-full hover:bg-gray-100 transition-colors cursor-pointer text-gray-600"
          >
            <ChevronRightIcon class="w-5 h-5" />
          </button>

          <button
            @click="emit('toggle-sidebar')"
            class="hidden lg:flex items-center justify-center p-2 ml-1 border border-gray-200 rounded-md transition-colors cursor-pointer"
            :class="
              isSidebarOpen
                ? 'bg-gray-100 text-[var(--color-primary)]'
                : 'bg-white text-gray-500 hover:bg-gray-50'
            "
            title="Afficher/Masquer la Timeline des devoirs"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-[20px] h-[20px]"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-linecap="round"
              stroke-linejoin="round"
              id="Layout-Sidebar--Streamline-Tabler"
              height="24"
              width="24"
            >
              <desc>Layout Sidebar Streamline Icon: https://streamlinehq.com</desc>
              <path
                d="M4 6a2 2 0 0 1 2 -2h12a2 2 0 0 1 2 2v12a2 2 0 0 1 -2 2H6a2 2 0 0 1 -2 -2z"
                stroke-width="2"
              ></path>
              <path d="m9 4 0 16" stroke-width="2"></path>
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- Modale de filtres -->
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

    <div class="flex-1 overflow-auto relative flex flex-col">
      <div class="sticky top-0 z-40 flex border-b border-gray-200 bg-white shadow-sm shrink-0">
        <div
          class="w-12 md:w-16 shrink-0 sticky left-0 z-50 bg-white border-r border-gray-100"
        ></div>

        <div class="flex-1 grid grid-cols-7">
          <div
            v-for="day in weekDays"
            :key="day.id"
            class="flex-1 py-2 text-sm flex flex-col items-center md:flex-row md:justify-center md:px-3 border-r border-gray-100 last:border-r-0"
            :class="
              day.isToday ? 'text-[var(--color-primary)] font-bold' : 'text-gray-600 font-medium'
            "
          >
            <span class="md:hidden">{{ day.name }}</span>
            <span class="hidden md:block md:px-1">{{ day.fullName }}</span>
            <span>{{ day.dateNumber }}</span>
          </div>
        </div>
      </div>

      <div class="h-4 shrink-0 bg-transparent"></div>

      <div class="flex flex-1 relative pb-6">
        <div class="w-12 md:w-16 shrink-0 sticky left-0 z-30 bg-white border-r border-gray-100">
          <div
            v-for="hour in hours.slice(0, -1)"
            :key="hour"
            class="relative border-transparent text-xs text-gray-400 text-right md:pr-2"
            :style="{ height: `${rowHeight}px` }"
          >
            <span class="absolute -top-2.5 right-2 bg-white px-1">
              {{ hour.toString().padStart(2, '0') }}:00
            </span>
          </div>
          <div class="relative text-xs text-gray-400 text-right md:pr-2">
            <span class="absolute -top-2.5 right-2 bg-white px-1">00:00</span>
          </div>
        </div>

        <div class="flex-1 grid grid-cols-7 relative bg-gray-50/30">
          <div class="absolute inset-0 pointer-events-none flex flex-col z-0">
            <div
              v-for="i in blocksCount"
              :key="'line-' + i"
              class="w-full border-b border-gray-200"
              :style="{ height: `${rowHeight}px` }"
            ></div>
          </div>

          <PlanningColumn
            v-for="day in weekDays"
            :key="'col-' + day.id"
            :day="day"
            :events="getEventsForDay(day.fullDateString)"
            :start-hour="START_HOUR"
            :row-height="rowHeight"
            class="z-10"
            @toggle-complete="updateStatus"
            @open-details="(evt) => emit('open-details', evt)"
            @request-add="(p) => emit('request-add', p)"
          />
        </div>
      </div>
    </div>
  </div>
</template>
