<template>
  <div class="flex flex-col h-full bg-white text-gray-800 font-sans select-none">
    <div class="flex">
      <div class="w-12 md:w-16 flex-shrink-0"></div>

      <div class="flex flex-1">
        <div
          v-for="(day, index) in weekDays"
          :key="index"
          class="flex-1 py-2 font-medium text-sm flex flex-col items-center md:flex-row md:px-3 border-b border-gray-200"
          :class="{ 'text-teal-600 font-bold': isToday(day.fullDate) }"
        >
          <span class="md:hidden">{{ day.name }}</span>

          <span class="hidden md:block md:px-1">{{ day.fullName }}</span>

          <span>{{ day.dateNumber }}</span>
        </div>
      </div>
    </div>

    <div class="flex flex-1 overflow-y-auto">
      <div class="w-12 md:w-16 flex-shrink-0 border-r border-gray-200 flex flex-col pt-1">
        <div
          v-for="hour in hours"
          :key="hour"
          class="h-16 relative flex justify-center text-xs text-gray-500"
        >
          <span class="absolute -top-2">{{ hour }}</span>
        </div>
      </div>

      <div class="flex flex-1 border-t border-gray-300">
        <div
          v-for="(day, index) in weekDays"
          :key="`col-${index}`"
          class="flex-1 border-r border-gray-300 relative"
        >
          <div
            v-for="hour in hours"
            :key="`line-${day.dateNumber}-${hour}`"
            class="h-16 border-b border-gray-200"
          ></div>

          <div
            v-if="isToday(day.fullDate) && isWithinCalendarHours()"
            class="absolute w-full border-t-2 border-red-500 z-20 pointer-events-none"
            :style="{ top: getCurrentTimePosition() }"
          >
            <div class="absolute -left-1.5 -top-1.5 w-3 h-3 bg-red-500 rounded-full"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

// Typage d'un jour de la semaine
interface WeekDay {
  name: string
  fullName: string
  dateNumber: number
  fullDate: Date
}

// Heures affichées dans le calendrier
const START_HOUR = 7
const hours = ref<string[]>([
  '07:00',
  '08:00',
  '09:00',
  '10:00',
  '11:00',
  '12:00',
  '13:00',
  '14:00',
  '15:00',
  '16:00',
  '17:00',
  '18:00',
  '19:00',
  '20:00',
])

// Référence pour stocker la date actuelle (mise à jour chaque minute pour la ligne rouge)
const now = ref<Date>(new Date())
let timer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  timer = setInterval(() => {
    now.value = new Date()
  }, 60000) // Rallume le calcul toutes les minutes
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})

// Génération dynamique des jours de la semaine courante (Lundi à Dimanche)
const weekDays = computed<WeekDay[]>(() => {
  const current = new Date(now.value)
  const dayIndex = current.getDay() // 0 = Dimanche, 1 = Lundi, etc.

  // Calcul de la distance par rapport au lundi de la semaine en cours
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
    return {
      name: dayName.short,
      fullName: dayName.full,
      dateNumber: nextDay.getDate(),
      fullDate: nextDay,
    }
  })
})

// Vérifie si une date correspond au jour d'aujourd'hui
const isToday = (date: Date): boolean => {
  const today = new Date()
  return (
    date.getDate() === today.getDate() &&
    date.getMonth() === today.getMonth() &&
    date.getFullYear() === today.getFullYear()
  )
}

// Vérifie si l'heure actuelle est dans la plage horaire du calendrier (07h - 21h)
const isWithinCalendarHours = (): boolean => {
  const currentHour = now.value.getHours()
  return currentHour >= START_HOUR && currentHour < 21
}

// Calcule la position exacte en pixels de la ligne rouge
const getCurrentTimePosition = (): string => {
  const currentHour = now.value.getHours()
  const currentMinutes = now.value.getMinutes()

  // Chaque bloc d'une heure fait h-16 = 64px
  const rowHeight = 64
  const hourOffset = currentHour - START_HOUR

  const positionY = hourOffset * rowHeight + (currentMinutes / 60) * rowHeight
  return `${positionY}px`
}
</script>
