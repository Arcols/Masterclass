<template>
  <div class="p-6 max-w-7xl mx-auto h-screen flex flex-col">
    <Calendar
      :events="schoolEvents"
      view="week"
      :enable-hotkeys="true"
      @time-slot-click="handleEmptySlotClick"
      @change-view="handleViewChange"
    >
      <div
        class="flex items-center justify-between mb-6 bg-white p-4 rounded-lg shadow-sm border border-gray-100"
      >
        <div class="flex items-center gap-2">
          <h2 class="ml-4 text-lg font-bold text-slate-800">Mon Planning</h2>
        </div>
      </div>

      <div class="flex-1 bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden">
        <CalendarWeekView />
      </div>
    </Calendar>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

// Import des briques de ton calendrier
import Calendar from '@/components/calendar/Calendar.vue'
import CalendarWeekView from '@/components/calendar/CalendarWeekView.vue'
//import CalendarPrevTrigger from '@/components/calendar/CalendarPrevTrigger.vue'
import CalendarNextTrigger from '@/components/calendar/CalendarNextTrigger.vue'
//import CalendarTodayTrigger from '@/components/calendar/CalendarTodayTrigger.vue'
//import CalendarViewTrigger from '@/components/calendar/CalendarViewTrigger.vue'

// (Optionnel) Icônes
import { ChevronLeftIcon, ChevronRightIcon } from '@heroicons/vue/24/outline'

// --- LOGIQUE MÉTIER ---

// 1. Les données (qui viendront plus tard de ton API Java)
const schoolEvents = ref([
  {
    id: '1',
    title: 'Algorithmique des Graphes',
    start: new Date(2026, 5, 15, 9, 0), // 15 Juin 2026, 09h00
    end: new Date(2026, 5, 15, 11, 0),
    type: 'event',
    location: 'Amphi A',
  },
  {
    id: '2',
    title: 'Projet Agile',
    start: new Date(2026, 5, 16, 14, 0),
    end: new Date(2026, 5, 16, 17, 0),
    type: 'event',
    location: 'Salle 102',
  },
])

// 2. Les actions utilisateur
const handleEmptySlotClick = (date: Date) => {
  console.log("L'utilisateur veut créer un cours à cette date :", date)
  // Action : Ouvrir la modale Figma "Nouveau Cours"
}

const handleViewChange = (newView: string) => {
  console.log('Passage à la vue :', newView)
}

// Simulation d'un appel API au backend
onMounted(async () => {
  // const response = await fetch('/api/v1/planning/events')
  // schoolEvents.value = await response.json()
})
</script>
