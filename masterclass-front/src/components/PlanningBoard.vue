<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { ChevronLeftIcon, ChevronRightIcon } from '@heroicons/vue/24/outline';
import PlanningColumn from './PlanningColumn.vue';
import type { EventData } from './EventCard.vue';
import mockEvents from '@/mocks/events.json';

// ── CONFIGURATION DE LA GRILLE ──
const START_HOUR = 7;
const END_HOUR = 24;
const MOBILE_ROW_HEIGHT = 50
const DESKTOP_ROW_HEIGHT = 80

const blocksCount = END_HOUR - START_HOUR;
const hours = Array.from({ length: blocksCount + 1 }, (_, i) => START_HOUR + i);

const events = ref<EventData[]>(mockEvents as EventData[])
const rowHeight = ref(MOBILE_ROW_HEIGHT)

const updateRowHeight = () => {
  rowHeight.value = window.innerWidth < 768 ? MOBILE_ROW_HEIGHT : DESKTOP_ROW_HEIGHT
}

// ── GESTION DES DATES ──
const currentDate = ref(new Date()); // Gère la semaine affichée (Navigation)
const now = ref(new Date());         // Gère l'instant T (Ligne rouge et "Aujourd'hui")
let timer: ReturnType<typeof setInterval> | null = null;

onMounted(() => {
  updateRowHeight()
  window.addEventListener('resize', updateRowHeight)

  timer = setInterval(() => {
    now.value = new Date()
  }, 60000)
});

onUnmounted(() => {
  if (timer) clearInterval(timer);
});

// Génération dynamique des jours de la semaine courante
const weekDays = computed(() => {
  const current = new Date(currentDate.value);
  const dayIndex = current.getDay(); // 0 = Dim, 1 = Lun...

  // Calcul de la distance par rapport au lundi
  const distanceToMonday = dayIndex === 0 ? -6 : 1 - dayIndex;

  const monday = new Date(current);
  monday.setDate(current.getDate() + distanceToMonday);

  const dayNames = [
    { short: 'Lun', full: 'Lundi' },
    { short: 'Mar', full: 'Mardi' },
    { short: 'Mer', full: 'Mercredi' },
    { short: 'Jeu', full: 'Jeudi' },
    { short: 'Ven', full: 'Vendredi' },
    { short: 'Sam', full: 'Samedi' },
    { short: 'Dim', full: 'Dimanche' },
  ];

  return dayNames.map((dayName, index) => {
    const nextDay = new Date(monday);
    nextDay.setDate(monday.getDate() + index);

    // Format YYYY-MM-DD pour filtrer les événements
    const yyyy = nextDay.getFullYear();
    const mm = String(nextDay.getMonth() + 1).padStart(2, '0');
    const dd = String(nextDay.getDate()).padStart(2, '0');
    const fullDateString = `${yyyy}-${mm}-${dd}`;

    // Vérifie si la colonne correspond à "Aujourd'hui"
    const isToday =
      nextDay.getDate() === now.value.getDate() &&
      nextDay.getMonth() === now.value.getMonth() &&
      nextDay.getFullYear() === now.value.getFullYear();

    return {
      id: `day-${index}`,
      name: dayName.short,
      fullName: dayName.full,
      dateNumber: nextDay.getDate(),
      fullDateString: fullDateString,
      isToday: isToday
    };
  });
});

// ── NAVIGATION & UTILITAIRES ──
const currentMonthYear = computed(() => {
  return currentDate.value.toLocaleDateString('fr-FR', { month: 'long', year: 'numeric' });
});

const prevWeek = () => {
  const newDate = new Date(currentDate.value);
  newDate.setDate(newDate.getDate() - 7);
  currentDate.value = newDate;
};

const nextWeek = () => {
  const newDate = new Date(currentDate.value);
  newDate.setDate(newDate.getDate() + 7);
  currentDate.value = newDate;
};

const getEventsForDay = (fullDateStr: string) => {
  return events.value.filter(e => e.date === fullDateStr);
};

const updateStatus = (id: string, newValue: boolean) => {
  const targetEvent = events.value.find(e => e.id === id);
  if (targetEvent) targetEvent.isCompleted = newValue;
};

const emit = defineEmits<{
  (e: 'open-details', event: EventData): void;
  (e: 'request-add', payload: { date: string; startTime: string }): void;
}>();

</script>

<template>
  <div
    class="flex flex-col h-full bg-white md:rounded-xl border border-gray-200 overflow-hidden shadow-sm"
  >
    <div class="flex items-center justify-between px-4 py-3 border-b border-gray-200 bg-white z-50">
      <h2 class="text-lg font-bold text-[var(--color-black)] capitalize">{{ currentMonthYear }}</h2>
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
      </div>
    </div>

    <div class="flex-1 overflow-auto relative flex flex-col">

      <div class="sticky top-0 z-40 flex border-b border-gray-200 bg-white shadow-sm shrink-0">
        <div
          class="w-14 md:w-16 shrink-0 sticky left-0 z-50 bg-white border-r border-gray-100"
        ></div>

        <div class="flex-1 grid grid-cols-7 min-w-[700px]">
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

        <div class="w-14 md:w-16 shrink-0 sticky left-0 z-30 bg-white border-r border-gray-100">
          <div
            v-for="hour in hours.slice(0, -1)"
            :key="hour"
            class="relative border-transparent text-xs text-gray-400 text-right pr-2"
            :style="{ height: `${rowHeight}px` }"
          >
            <span class="absolute -top-2.5 right-2 bg-white px-1">
              {{ hour.toString().padStart(2, '0') }}:00
            </span>
          </div>
          <div class="relative text-xs text-gray-400 text-right pr-2">
            <span class="absolute -top-2.5 right-2 bg-white px-1">00:00</span>
          </div>
        </div>

        <div class="flex-1 grid grid-cols-7 min-w-[700px] relative bg-gray-50/30">
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
