<script setup>
import { ref, computed } from 'vue'

// 1. État : Date de référence (aujourd'hui par défaut)
const currentDate = ref(new Date())

// 2. Calculer les 5 jours de la semaine courante (Lundi au Vendredi)
const weekDays = computed(() => {
  const date = new Date(currentDate.value)
  const day = date.getDay()
  // Trouver le lundi de la semaine en cours
  const diff = date.getDate() - day + (day === 0 ? -6 : 1)
  const monday = new Date(date.setDate(diff))

  // Générer un tableau avec les 5 jours
  return Array.from({ length: 5 }, (_, i) => {
    const d = new Date(monday)
    d.setDate(monday.getDate() + i)
    return d
  })
})

// Formater la date pour l'affichage (ex: "Lun 10 Juin")
const formatDate = (date) => {
  return date.toLocaleDateString('fr-FR', { weekday: 'short', day: 'numeric', month: 'short' })
}

const nextWeek = () => {
  const next = new Date(currentDate.value)
  next.setDate(next.getDate() + 7)
  currentDate.value = next
}

const prevWeek = () => {
  const prev = new Date(currentDate.value)
  prev.setDate(prev.getDate() - 7)
  currentDate.value = prev
}

// Les heures affichées sur le côté (8h à 18h)
const hours = Array.from({ length: 11 }, (_, i) => i + 8)

// Les données de test
const events = ref([
  {
    id: 1,
    title: 'Mathématiques',
    date: '2026-06-10',
    start: '09:00',
    end: '11:00',
    color: 'bg-blue-500',
  },
  {
    id: 2,
    title: 'Projet Agile',
    date: '2026-06-11',
    start: '14:30',
    end: '16:00',
    color: 'bg-green-500',
  },
])

// Fonction clé : Convertir des heures (HH:MM) en pixels
const getEventStyle = (event) => {
  const [startH, startM] = event.start.split(':').map(Number)
  const [endH, endM] = event.end.split(':').map(Number)

  const startInHours = startH + startM / 60
  const endInHours = endH + endM / 60

  const top = (startInHours - 8) * 60 // Décalage depuis 8h
  const height = (endInHours - startInHours) * 60 // Durée du cours

  return {
    top: `${top}px`,
    height: `${height}px`,
    position: 'absolute',
    width: '90%', // Laisse une petite marge à droite
    left: '5%',
  }
}

// Filtrer les événements pour une colonne de jour spécifique
const getEventsForDay = (date) => {
  const dateString = date.toISOString().split('T')[0]
  return events.value.filter((e) => e.date === dateString)
}
</script>

<template>
  <div class="p-4 max-w-6xl mx-auto bg-white rounded shadow">
    <!-- Header : Navigation -->
    <div class="flex justify-between items-center mb-4">
      <button @click="prevWeek" class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">
        Précédent
      </button>
      <h2 class="text-xl font-bold font-sans capitalize">
        Semaine du {{ formatDate(weekDays[0]) }}
      </h2>
      <button @click="nextWeek" class="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300">
        Suivant
      </button>
    </div>

    <!-- Le Calendrier -->
    <div class="flex border-t border-l border-gray-200">
      <!-- Colonne des Heures (Axe Y) -->
      <div class="w-16 flex-shrink-0">
        <div class="h-10 border-b border-r border-gray-200 bg-gray-50"></div>
        <!-- Case vide coin haut gauche -->
        <div
          v-for="hour in hours"
          :key="hour"
          class="h-[60px] border-b border-r border-gray-200 text-xs text-gray-500 text-right pr-2 pt-1"
        >
          {{ hour }}h00
        </div>
      </div>

      <!-- Colonnes des Jours (Axe X) -->
      <div v-for="day in weekDays" :key="day" class="flex-1 relative">
        <!-- En-tête du jour -->
        <div
          class="h-10 border-b border-r border-gray-200 bg-gray-50 text-center font-medium pt-2 capitalize"
        >
          {{ formatDate(day) }}
        </div>

        <!-- Corps de la colonne (C'est ici qu'on positionne les cours) -->
        <div
          class="relative border-r border-gray-200"
          :style="{ height: `${hours.length * 60}px` }"
        >
          <!-- Lignes horizontales de fond -->
          <div
            v-for="hour in hours"
            :key="`bg-${hour}`"
            class="absolute w-full border-t border-gray-100"
            :style="{ top: `${(hour - 8) * 60}px` }"
          ></div>

          <!-- Les Événements du jour -->
          <div
            v-for="event in getEventsForDay(day)"
            :key="event.id"
            :class="[
              'rounded-md p-2 text-white text-xs overflow-hidden shadow-sm cursor-pointer hover:opacity-90',
              event.color,
            ]"
            :style="getEventStyle(event)"
          >
            <div class="font-bold">{{ event.title }}</div>
            <div>{{ event.start }} - {{ event.end }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
