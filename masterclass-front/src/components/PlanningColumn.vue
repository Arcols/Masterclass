<script setup lang="ts">
import { computed, ref, onMounted, onUnmounted } from 'vue';
import EventCard, { type EventData } from './EventCard.vue';
import { PlanningDay } from '@/types/planningDay.ts';

const props = defineProps<{
  day: PlanningDay;
  events: EventData[];
  startHour: number;
  rowHeight: number;
}>();

const emit = defineEmits<{
  (e: 'toggle-complete', id: string, newValue: boolean): void;
}>();

const getEventStyle = (event: EventData) => {
  const [startH, startM] = event.startTime.split(':').map(Number);
  let [endH, endM] = event.endTime.split(':').map(Number);

  if (endH === 0 && endM === 0) endH = 24;

  const topPx = ((startH - props.startHour) + (startM / 60)) * props.rowHeight;
  const heightPx = ((endH - startH) + ((endM - startM) / 60)) * props.rowHeight;

  return { top: `${topPx}px`, height: `${heightPx}px` };
};

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
  <div class="relative w-full h-full border-r border-gray-100 last:border-r-0">
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
      v-for="event in events"
      :key="event.id"
      class="absolute left-0 w-full px-1 py-0.5 z-10 transition-transform hover:scale-[1.02] hover:z-30"
      :style="getEventStyle(event)"
    >
      <EventCard
        :event="event"
        layout="calendar"
        @toggle-complete="(id, val) => emit('toggle-complete', id, val)"
      />
    </div>
  </div>
</template>
