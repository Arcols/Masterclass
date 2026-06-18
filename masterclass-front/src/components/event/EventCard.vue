<script setup lang="ts">
import { computed } from 'vue'
import { ClockIcon, MapPinIcon } from '@heroicons/vue/24/outline'
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
      isList ? 'py-3 md:py-4 pr-4 pl-5' : 'md:p-2 md:pl-3 p-1 pl-2',
      event.isCompleted
        ? 'bg-[var(--color-event-done-bg)] opacity-65'
        : 'bg-[var(--color-event-not-done-bg)]',
    ]"
  >
    <div
      class="absolute left-0 top-0 bottom-0 w-1 md:w-1.5"
      :style="{ backgroundColor: leftBarColor }"
    ></div>

      <div class="flex justify-between items-start md:gap-2" :class="isCompact ? 'gap-1' : ''">
      <div class="flex flex-col items-start gap-1 min-w-0">

        <!-- Affichage de la matière (s'il y en a une) -->
        <span
          v-if="event.subject"
          class="hidden md:block font-semibold text-gray-500 w-full whitespace-normal break-words md:truncate md:whitespace-nowrap"
          :class="isList ? 'text-sm' : 'text-[11px]'"
        >
          {{ event.subject }}
        </span>

        <!-- Affichage du TITRE (s'il n'y a PAS de matière) -->
        <div
          v-else
          class="flex w-full"
          :class="isList ? 'flex-row items-start md:items-center gap-1' : 'flex-col md:flex-row items-start md:items-center gap-0.5 md:gap-1.5'"
        >
          <!-- Étoile cachée sur mobile -->
          <StarSolid
            v-if="event.isFavorite"
            class="hidden md:block w-4 h-4 text-[var(--color-event-favorite-selected)] shrink-0"
          />
          <span
            class="font-bold text-[var(--color-black)] w-full whitespace-normal break-words md:truncate md:whitespace-nowrap"
            :class="[
              isList ? 'text-base md:text-lg' : 'md:text-[13px] text-[9px]',
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
    <div class="flex justify-between items-center md:mt-1" :class="isCompact ? 'mt-0' : ''">

      <!-- Affichage du TITRE (s'il Y A un sujet) -->
      <div
        v-if="event.subject && !isCompact"
        class="flex min-w-0 pr-2"
        :class="isList ? 'flex-row items-center gap-1' : 'flex-col md:flex-row items-start md:items-center gap-0.5 md:gap-1.5'"
      >
        <!-- Étoile cachée sur mobile (hidden md:block) -->
        <StarSolid
          v-if="event.isFavorite"
          class="hidden md:block w-4 h-4 text-[var(--color-event-favorite-selected)] shrink-0"
        />
        <span
          class="flex font-bold text-[var(--color-black)] whitespace-normal break-words md:truncate md:whitespace-nowrap"
          :class="[
            isList ? 'text-base md:text-lg' : 'md:text-[13px] text-[9px]',
            { 'line-through text-gray-600': event.isCompleted },
          ]"
        >
          {{ event.title }}
        </span>
      </div>

      <div
        v-else-if="!isList && event.location && !isCompact"
        class="flex items-center text-gray-500 text-[11px]"
      >
        <MapPinIcon class="hidden md:block w-3 h-3 mr-1" />
        {{ event.location }}
      </div>

      <input
        v-if="isDevoir && !isCompact"
        class="hidden md:block"
        type="checkbox"
        :class="[
          'rounded border-2 border-gray-400 text-primary focus:ring-primary shrink-0 cursor-pointer transition-all',
          isList ? 'w-6 h-6' : 'w-4 h-4',
        ]"
        :checked="event.isCompleted"
        :aria-label="event.isCompleted ? 'Marquer comme non terminé' : 'Marquer comme terminé'"
        @click.stop
        @keydown.enter.stop
        @keydown.space.stop
        @change="emit('toggle-complete', event.id, ($event.target as HTMLInputElement).checked)"
      />
    </div>

    <div class="hidden md:flex justify-between items-center mt-auto" :class="[isList ? 'pt-4' : 'pt-2', isCompact ? 'hidden' : '']">
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
