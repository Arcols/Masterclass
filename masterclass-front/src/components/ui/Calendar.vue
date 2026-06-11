<script setup lang="ts">
import { ref, provide, watch } from 'vue'
import { fr } from 'date-fns/locale'
import { onKeyStroke } from '@vueuse/core'
import { CalendarInjectionKey, type View, type CalendarEvent } from '@/composable/useCalendar'

const props = withDefaults(
  defineProps<{
    defaultDate?: Date
    events?: CalendarEvent[]
    view?: View
    locale?: Locale
    enableHotkeys?: boolean
  }>(),
  {
    defaultDate: () => new Date(),
    events: () => [],
    view: 'week',
    locale: () => fr,
    enableHotkeys: true,
  },
)

const emit = defineEmits(['changeView', 'timeSlotClick'])

const currentView = ref<View>(props.view)
const currentDate = ref(props.defaultDate)
const currentEvents = ref<CalendarEvent[]>(props.events)

watch(
  () => props.events,
  (newEvents) => {
    currentEvents.value = newEvents
  },
  { deep: true },
)

const setView = (v: View) => {
  currentView.value = v
  emit('changeView', v)
}

const setDate = (d: Date) => {
  currentDate.value = d
}

// Raccourcis clavier via VueUse
if (props.enableHotkeys) {
  onKeyStroke('m', () => setView('month'))
  onKeyStroke('w', () => setView('week'))
  onKeyStroke('d', () => setView('day'))
  onKeyStroke('t', () => setDate(new Date()))
}

// Fournir le contexte aux enfants
provide(CalendarInjectionKey, {
  view: currentView,
  date: currentDate,
  events: currentEvents,
  locale: props.locale,
  today: new Date(),
  setView,
  setDate,
  onTimeSlotClick: (d: Date) => emit('timeSlotClick', d),
})
</script>

<template>
  <div class="calendar-root">
    <slot />
  </div>
</template>
