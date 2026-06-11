<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import {
  addDays,
  startOfWeek,
  format,
  isToday,
  startOfDay,
  endOfDay,
  differenceInMinutes,
} from 'date-fns'
import { useCalendar } from '@/composable/useCalendar'
//import CalendarEventCard from './CalendarEventCard.vue' // Ton composant de carte
import { cn } from '@/lib/utils'

const { view, date, locale, events, onTimeSlotClick } = useCalendar()
const HOUR_HEIGHT = 3.5 // rem
const scrollRef = ref<HTMLElement | null>(null)

// Scroll à 8h au montage
onMounted(() => {
  if (scrollRef.value) scrollRef.value.scrollTop = 448
})

// 1. Calcul des jours de l'en-tête
const headerDays = computed(() => {
  return Array.from({ length: 7 }).map((_, i) =>
    addDays(startOfWeek(date.value, { weekStartsOn: 1 }), i),
  )
})

// 2. Calcul des événements positionnés (La logique de Clustering traduite)
const processedDays = computed(() => {
  return headerDays.value.map((dayDate) => {
    const dayStart = startOfDay(dayDate)
    const dayEnd = endOfDay(dayDate)

    // Filtrer et ajuster les dates
    let dayEvents = events.value
      .filter(
        (e) =>
          (e.start >= dayStart && e.start <= dayEnd) ||
          (e.end >= dayStart && e.end <= dayEnd) ||
          (e.start < dayStart && e.end > dayEnd),
      )
      .map((e) => ({
        ...e,
        visibleStart: e.start < dayStart ? dayStart : e.start,
        visibleEnd: e.end > dayEnd ? dayEnd : e.end,
      }))

    dayEvents.sort((a, b) => a.visibleStart.getTime() - b.visibleStart.getTime())

    // Logique de Clustering
    const clusters: (typeof dayEvents)[] = []
    let currentCluster: typeof dayEvents = []
    let clusterEnd = -1

    dayEvents.forEach((event) => {
      if (currentCluster.length === 0) {
        currentCluster.push(event)
        clusterEnd = event.visibleEnd.getTime()
      } else {
        if (event.visibleStart.getTime() < clusterEnd) {
          currentCluster.push(event)
          clusterEnd = Math.max(clusterEnd, event.visibleEnd.getTime())
        } else {
          clusters.push(currentCluster)
          currentCluster = [event]
          clusterEnd = event.visibleEnd.getTime()
        }
      }
    })
    if (currentCluster.length > 0) clusters.push(currentCluster)

    // Calcul des largeurs et positions
    const finalEvents = clusters.flatMap((cluster) => {
      const columns: (typeof cluster)[] = []
      const clusterWithPos = cluster.map((event) => {
        let colIndex = -1
        for (let c = 0; c < columns.length; c++) {
          const lastEvent = columns[c][columns[c].length - 1]
          if (event.visibleStart >= lastEvent.visibleEnd) {
            colIndex = c
            columns[c].push(event)
            break
          }
        }
        if (colIndex === -1) {
          colIndex = columns.length
          columns.push([event])
        }
        return { ...event, colIndex }
      })

      const colWidth = 100 / columns.length
      return clusterWithPos.map((event) => {
        const startMins = differenceInMinutes(event.visibleStart, dayStart)
        const durMins = differenceInMinutes(event.visibleEnd, event.visibleStart)

        return {
          ...event,
          style: {
            top: `${(startMins / 60) * HOUR_HEIGHT}rem`,
            height: `${Math.max((durMins / 60) * HOUR_HEIGHT, HOUR_HEIGHT / 2)}rem`, // Min 30 min
            left: `${event.colIndex * colWidth}%`,
            width: `${colWidth}%`,
            position: 'absolute' as const,
          },
        }
      })
    })

    return { date: dayDate, events: finalEvents }
  })
})
</script>

<template>
  <div v-if="view === 'week'" ref="scrollRef" class="relative flex h-full flex-col overflow-auto">
    <div class="bg-card sticky top-0 z-30 mb-3 flex border-b">
      <div class="w-12 shrink-0"></div>
      <div
        v-for="(day, i) in headerDays"
        :key="day.toString()"
        :class="
          cn(
            'flex flex-1 items-center justify-center pb-2 text-sm',
            [5, 6].includes(i) && 'opacity-50',
          )
        "
      >
        {{ format(day, 'EEEE', { locale }) }}
      </div>
    </div>

    <div class="relative flex flex-1">
      <div class="sticky left-0 z-20 w-12 shrink-0 bg-background"></div>

      <div class="relative flex-1 h-[84rem]">
        <div class="pointer-events-none absolute inset-0 z-10 grid grid-cols-7">
          <div
            v-for="dayData in processedDays"
            :key="dayData.date.toString()"
            class="relative h-full border-l text-sm"
          >
            <div
              v-for="event in dayData.events"
              :key="event.id"
              class="pointer-events-auto"
              :style="event.style"
            >
              <CalendarEventCard :event="event" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
