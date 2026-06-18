<script setup lang="ts">
import { computed, ref, onMounted, onUnmounted } from 'vue';
import EventCard, { type EventData } from '../event/EventCard.vue';
import type { PlanningDay } from '@/types/planningDay.ts';

const props = defineProps<{
  day: PlanningDay;
  events: EventData[];
  startHour: number;
  rowHeight: number;
}>();

const emit = defineEmits<{
  (e: 'toggle-complete', id: string, newValue: boolean): void;
  (e: 'open-details', event: EventData): void;
  (e: 'request-add', payload: { date: string; startTime: string }): void;
}>();

function handleDblClick(ev: MouseEvent):void {
  // compute time from click position
  const target = ev.currentTarget as HTMLElement
  const rect = target.getBoundingClientRect()
  const y = Math.min(Math.max(ev.clientY - rect.top, 0), rect.height)
  const hoursFloat = y / props.rowHeight + props.startHour
  let h = Math.floor(hoursFloat)
  const minutes = (hoursFloat - h) * 60  // snap to 30 minutes

  let m = minutes < 15 ? 0 : minutes < 45 ? 30 : 0
  if (minutes >= 45) h += 1
  // clamp to the last selectable slot (23:30)
  if (h < props.startHour) h = props.startHour
  if (h > 23) h = 23
  if (h === 23 && m > 30) m = 30

  const hh = String(h).padStart(2, '0')
  const mm = String(m).padStart(2, '0')
  emit('request-add', { date: props.day.fullDateString, startTime: `${hh}:${mm}` })
}

type LayoutEvent = EventData & {
  laneIndex: number
  laneCount: number
  compact: boolean
}

const getEventMinutes = (time: string) => {
  const [hours = 0, minutes = 0] = time.split(':').map(Number)
  return hours * 60 + minutes
}

const getEventStyle = (event: LayoutEvent) => {
  const [startH = 0, startM = 0] = event.startTime.split(':').map(Number);
  let [endH = 0, endM = 0] = event.endTime.split(':').map(Number);

  if (endH === 0 && endM === 0) endH = 24;

  const topPx = ((startH - props.startHour) + (startM / 60)) * props.rowHeight;
  const heightPx = ((endH - startH) + ((endM - startM) / 60)) * props.rowHeight;

  const laneWidth = 100 / event.laneCount
  const leftPx = event.laneIndex * laneWidth

  return {
    top: `${topPx}px`,
    height: `${heightPx}px`,
    left: `${leftPx}%`,
    width: `${laneWidth}%`,
    zIndex: `${10 + event.laneIndex}`,
  };
};

const isCompactEvent = (event: EventData) => {
  const [startH = 0, startM = 0] = event.startTime.split(':').map(Number)
  const [endH = 0, endM = 0] = event.endTime.split(':').map(Number)
  const startMinutes = startH * 60 + startM
  const endMinutes = endH * 60 + endM
  return endMinutes - startMinutes <= 30
}

const layoutEvents = computed<LayoutEvent[]>(() => {
  const sortedEvents = [...props.events].sort((a, b) => {
    const startDelta = getEventMinutes(a.startTime) - getEventMinutes(b.startTime)
    if (startDelta !== 0) return startDelta
    return getEventMinutes(a.endTime) - getEventMinutes(b.endTime)
  })

  const laidOut: LayoutEvent[] = []
  let currentCluster: LayoutEvent[] = []
  let clusterEnd = -1

  const flushCluster = () => {
    if (currentCluster.length === 0) return

    const laneEnds: number[] = []
    for (const event of currentCluster) {
      const start = getEventMinutes(event.startTime)
      const end = getEventMinutes(event.endTime)
      let laneIndex = laneEnds.findIndex((laneEnd) => laneEnd <= start)

      if (laneIndex === -1) {
        laneIndex = laneEnds.length
        laneEnds.push(end)
      } else {
        laneEnds[laneIndex] = end
      }

      laidOut.push({
        ...event,
        laneIndex,
        laneCount: 0,
        compact: isCompactEvent(event),
      })
    }

    const laneCount = Math.max(1, laneEnds.length)
    for (const event of laidOut.slice(-currentCluster.length)) {
      event.laneCount = laneCount
      event.compact = event.compact || laneCount > 1
    }

    currentCluster = []
    clusterEnd = -1
  }

  for (const event of sortedEvents) {
    const start = getEventMinutes(event.startTime)
    const end = getEventMinutes(event.endTime)
    if (currentCluster.length === 0) {
      currentCluster.push({ ...event, laneIndex: 0, laneCount: 0, compact: isCompactEvent(event) })
      clusterEnd = end
      continue
    }

    if (start < clusterEnd) {
      currentCluster.push({ ...event, laneIndex: 0, laneCount: 0, compact: isCompactEvent(event) })
      clusterEnd = Math.max(clusterEnd, end)
      continue
    }

    flushCluster()
    currentCluster.push({ ...event, laneIndex: 0, laneCount: 0, compact: isCompactEvent(event) })
    clusterEnd = end
  }

  flushCluster()
  return laidOut
})

// ── LOGIQUE DE LA LIGNE ROUGE EN TEMPS RÉEL ──
const now = ref(new Date());
let timer: ReturnType<typeof setInterval> | null = null;

onMounted(() => {
  // Met à jour l'heure toutes les minutes pour faire bouger la ligne
  timer = setInterval(() => {
    now.value = new Date();
  }, 60000);
});
onUnmounted(() => {
  if (timer) clearInterval(timer);
});

const currentTimeTop = computed(() => {
  const currentH = now.value.getHours();
  const currentM = now.value.getMinutes();

  // Si on est en dehors des heures du planning, on ne l'affiche pas (optionnel)
  if (currentH < props.startHour) return '-10px';

  return `${((currentH - props.startHour) + (currentM / 60)) * props.rowHeight}px`;
});
</script>

<template>
    <div class="relative w-full h-full border-r border-gray-100 last:border-r-0" @dblclick="handleDblClick">
    <div
      v-if="day.isToday"
      class="absolute left-0 w-full z-20 pointer-events-none"
      :style="{ top: currentTimeTop }"
    >
      <div class="h-[2px] bg-red-500 w-full relative">
        <div class="w-2.5 h-2.5 bg-red-500 rounded-full absolute -left-1.5 -top-1"></div>
      </div>
    </div>

    <div
      v-for="event in layoutEvents"
      :key="event.id"
      class="absolute px-0.5 py-0.5 transition-transform hover:scale-[1.01] hover:z-30"
      :style="getEventStyle(event)"
    >
      <EventCard
        :event="event"
        layout="calendar"
        :compact="event.compact"
        @toggle-complete="(id, val) => emit('toggle-complete', id, val)"
        @open-details="(evt) => emit('open-details', evt)"
      />
    </div>
  </div>
</template>
