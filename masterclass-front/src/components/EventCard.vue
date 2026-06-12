<script setup lang="ts">
import { computed } from 'vue'
import { ClockIcon, MapPinIcon } from '@heroicons/vue/24/outline'
import EventBadge from './EventBadge.vue'
import PriorityIndicator from './PriorityIndicator.vue'

export interface EventData {
  id: string;
  type: 'devoir' | 'activite' | 'sport';
  title: string;
  subject?: string | null;
  description?: string | null;
  date: string;
  startTime: string;
  endTime: string;
  location?: string;
  priority?: 'urgent' | 'normal' | 'faible' | null;
  group: string;
  isCompleted: boolean;
}

const props = withDefaults(
  defineProps<{
    event: EventData
    layout?: 'list' | 'calendar'
  }>(),
  { layout: 'list' },
)

const emit = defineEmits<{
  (e: 'toggle-complete', id: string, newValue: boolean): void;
  (e: 'open-details', event: EventData): void;
}>();

const isList = computed(() => props.layout === 'list')
const isDevoir = computed(() => props.event.type === 'devoir')

const leftBarColor = computed(() => `var(--color-${props.event.type}-stroke)`)
</script>

<template>
  <div
    @click="emit('open-details', event)"
    class="relative flex h-full flex-col rounded-xl border border-gray-100 shadow-sm overflow-hidden transition-all duration-200 cursor-pointer"
    :class="[
      isList ? 'p-4 pl-5' : 'p-2 pl-3',
      event.isCompleted
        ? 'bg-[var(--color-event-done-bg)] opacity-65'
        : 'bg-[var(--color-event-not-done-bg)]',
    ]"
  >
    <div
      class="absolute left-0 top-0 bottom-0 w-1.5"
      :style="{ backgroundColor: leftBarColor }"
    ></div>

    <div class="flex justify-between items-start gap-2">
      <div class="flex flex-col items-start gap-1 min-w-0">
        <PriorityIndicator :priority="event.priority" class="hidden md:flex" />

        <span
          v-if="event.subject"
          class="hidden md:block font-semibold text-gray-500 truncate w-full"
          :class="isList ? 'text-sm' : 'text-[11px]'"
        >
          {{ event.subject }}
        </span>
        <span
          v-else
          class="font-bold text-[var(--color-black)] truncate w-full"
          :class="[
            isList ? 'text-lg' : 'text-sm',
            { 'line-through text-gray-400': event.isCompleted },
          ]"
        >
          {{ event.title }}
        </span>
      </div>

      <EventBadge :type="event.type" class="hidden md:block shrink-0" />
    </div>

    <div class="flex justify-between items-center mt-1">
      <span
        v-if="event.subject"
        class="flex font-bold text-[var(--color-black)] truncate pr-2"
        :class="[
          isList ? 'text-xl' : 'text-[13px]',
          { 'line-through text-gray-600': event.isCompleted },
        ]"
      >
        {{ event.title }}
      </span>

      <div
        v-else-if="!isList && event.location"
        class="flex items-center text-gray-500 text-[11px]"
      >
        <MapPinIcon class="w-3 h-3 mr-1" />
        {{ event.location }}
      </div>

      <input
        v-if="isDevoir"
        class="hidden md:block"
        type="checkbox"
        :class="[
          'rounded border-2 border-gray-400 text-primary focus:ring-primary shrink-0 cursor-pointer transition-all',
          isList ? 'w-6 h-6' : 'w-4 h-4',
        ]"
        :checked="event.isCompleted"
        :aria-label="event.isCompleted ? 'Marquer comme non terminé' : 'Marquer comme terminé'"
        @click.stop
        @change="emit('toggle-complete', event.id, ($event.target as HTMLInputElement).checked)"
      />
    </div>

    <div class="flex justify-between items-center mt-auto" :class="isList ? 'pt-4' : 'pt-2'">
      <div
        class="hidden md:flex items-center text-gray-500 truncate pr-2"
        :class="isList ? 'text-sm' : 'text-[11px]'"
      >
        <ClockIcon class="shrink-0 mr-1.5" :class="isList ? 'w-4 h-4' : 'w-3 h-3'" />

        <span v-if="isList">
          {{ event.date }} &bull; {{ event.startTime }} - {{ event.endTime }}
        </span>
        <span v-else> {{ event.startTime }} - {{ event.endTime }} </span>
      </div>

      <span
        class="hidden md:block bg-gray-100 text-[var(--color-black)] px-2 py-0.5 rounded-full font-medium shrink-0"
        :class="isList ? 'text-xs' : 'text-[9px]'"
      >
        {{ event.group }}
      </span>
    </div>
  </div>
</template>
