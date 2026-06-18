<script setup lang="ts">
import { computed } from 'vue'
import { ClockIcon } from '@heroicons/vue/24/outline'
import { StarIcon as StarSolid } from '@heroicons/vue/24/solid'
import EventBadge from './EventBadge.vue'

export interface EventData {
  id: string;
  type: 'devoir' | 'activite' | 'sport' | 'examen';
  title: string;
  subject?: string | null;
  description?: string | null;
  date: string;
  startTime: string;
  endTime: string;
  submissionLink?: string;
  location?: string;
  group: string;
  isCompleted: boolean;
  isFavorite?: boolean;
}

const props = withDefaults(
  defineProps<{
    event: EventData
    layout?: 'list' | 'calendar'
    compact?: boolean
  }>(),
  { layout: 'list' },
)

const emit = defineEmits<{
  (e: 'toggle-complete', id: string, newValue: boolean): void;
  (e: 'open-details', event: EventData): void;
}>();

const isList = computed(() => props.layout === 'list')
const isDevoir = computed(() => props.event.type === 'devoir')
const isCompact = computed(() => props.layout === 'calendar' && props.compact === true)
const showSubject = computed(() => !!props.event.subject && !isCompact.value)
const showFooter = computed(() => !isCompact.value)

const leftBarColor = computed(() => `var(--color-tag-${props.event.type}-border)`)
</script>

<template>
  <div
    role="button"
    tabindex="0"
    @click="emit('open-details', event)"
    @keydown.enter="emit('open-details', event)"
    @keydown.space.prevent="emit('open-details', event)"
    class="relative flex h-full flex-col rounded-xl border border-gray-100 shadow-sm overflow-hidden transition-all duration-200 cursor-pointer focus:outline-none focus-visible:ring-2 focus-visible:ring-[var(--color-primary)] focus-visible:ring-offset-2"
    :class="[
      isList ? 'py-3 md:py-4 pr-4 pl-5' : 'md:p-1 md:pl-3 p-1 pl-2',
      isCompact ? 'p-1 pl-2' : '',
      event.isCompleted
        ? 'bg-[var(--color-event-done-bg)] opacity-65'
        : 'bg-[var(--color-event-not-done-bg)]',
    ]"
  >
    <div
      class="absolute left-0 top-0 bottom-0 w-1 md:w-1.5"
      :class="[
        isCompact ? 'w-1.5 md:1-5' : ''
      ]"
      :style="{ backgroundColor: leftBarColor }"
    ></div>

    <div class="flex justify-between items-start md:gap-2" :class="isCompact ? 'gap-1' : ''">
      <div class="flex flex-col items-start gap-0.5 min-w-0">
        <span
          v-if="showSubject"
          class="hidden md:block font-semibold text-gray-500 w-full whitespace-normal break-words"
          :class="isList ? 'text-sm' : 'text-[11px]'"
        >
          {{ event.subject }}
        </span>

        <div
          class="flex w-full"
          :class="isList ? 'flex-row items-start md:items-center gap-1' : 'flex-col md:flex-row items-start md:items-center gap-0.5 md:gap-1.5'"
        >
          <StarSolid
            v-if="event.isFavorite && !isCompact"
            class="hidden md:block w-4 h-4 text-[var(--color-event-favorite-selected)] shrink-0"
          />
          <span
            class="font-bold text-[var(--color-black)] w-full whitespace-normal break-words leading-tight"
            :class="[
              isList ? 'text-base md:text-lg' : isCompact ? 'hidden md:block md:text-[11px]' : 'md:text-[11px] text-[9px]',
              { 'line-through text-gray-400': event.isCompleted },
            ]"
          >
            {{ event.title }}
          </span>
        </div>
      </div>

      <EventBadge v-if="!isCompact" :type="event.type" class="hidden md:block md:shrink-0" />
    </div>
    <!-- Affichage des détails de l'événement -->
    <div v-if="showFooter" class="hidden md:flex justify-between items-center mt-auto" :class="isList ? 'pt-4' : 'pt-2'">
      <div
        class="hidden md:flex items-center text-gray-500 truncate pr-2"
        :class="isList ? 'text-xs md:text-sm' : 'text-[11px]'"
      >
        <ClockIcon class="shrink-0 mr-1.5" :class="isList ? 'w-4 h-4' : 'w-3 h-3'" />

        <span v-if="isList">
          {{ event.date }} &bull; {{ event.startTime }} - {{ event.endTime }}
        </span>
        <span v-else> {{ event.startTime }} - {{ event.endTime }} </span>
      </div>

      <span
        class="hidden md:block bg-gray-100 text-[var(--color-black)] px-2 py-0.5 rounded-full font-medium shrink-0"
        :class="isList ? 'text-[10px] md:text-xs' : 'text-[9px]'"
      >
        {{ event.group }}
      </span>
    </div>
  </div>
</template>
