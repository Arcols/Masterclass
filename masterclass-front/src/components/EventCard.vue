<script setup lang="ts">
import { computed } from 'vue';

export interface EventData {
  id: string;
  type: 'devoir' | 'activite' | 'sport';
  title: string;
  subject?: string | null;
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
    event: EventData;
    layout?: 'list' | 'calendar';
  }>(),
  {
    layout: 'list',
  }
);

const emit = defineEmits<{
  (e: 'toggle-complete', id: string, newValue: boolean): void;
}>();

const isList = computed(() => props.layout === 'list');
const isDevoir = computed(() => props.event.type === 'devoir');

// La barre latérale prend toujours la couleur de la bordure (stroke) de la catégorie
const leftBarColor = computed(() => `var(--stroke-${props.event.type})`);

// Gestion des badges unifiée à 3 variables (txt, bg, stroke)
const badgeData = computed(() => {
  if (props.event.type === 'activite') {
    return {
      text: 'ACTIVITÉ',
      style: {
        backgroundColor: 'var(--bg-activite)',
        color: 'var(--txt-activite)',
        borderColor: 'var(--stroke-activite)'
      }
    };
  }
  if (props.event.type === 'sport') {
    return {
      text: 'SPORT',
      style: {
        backgroundColor: 'var(--bg-sport)',
        color: 'var(--txt-sport)',
        borderColor: 'var(--stroke-sport)'
      }
    };
  }
  if (props.event.priority) {
    return {
      text: props.event.priority.toUpperCase(),
      style: {
        backgroundColor: `var(--bg-${props.event.priority})`,
        color: `var(--txt-${props.event.priority})`,
        borderColor: `var(--stroke-${props.event.priority})`
      }
    };
  }
  return null;
});
</script>

<template>
  <div
    class="relative flex flex-col rounded-xl border border-gray-100 shadow-sm overflow-hidden transition-all duration-200"
    :class="[
      isList ? 'p-4 pl-5' : 'p-2 pl-3',
      event.isCompleted ? 'bg-gray-100 opacity-65' : 'bg-white'
    ]"
  >
    <div
      class="absolute left-0 top-0 bottom-0 w-1.5"
      :style="{ backgroundColor: leftBarColor }"
    ></div>

    <div class="flex justify-between items-start gap-2">
      <span
        v-if="event.subject"
        class="font-semibold text-gray-500 truncate"
        :class="isList ? 'text-sm' : 'text-[11px]'"
      >
        {{ event.subject }}
      </span>
      <span
        v-else
        class="font-bold text-gray-900 truncate"
        :class="[
          isList ? 'text-lg' : 'text-sm',
          { 'line-through text-gray-400': event.isCompleted }
        ]"
      >
        {{ event.title }}
      </span>

      <span
        v-if="badgeData"
        class="px-2 py-0.5 rounded text-[10px] font-bold tracking-wide border shrink-0"
        :style="badgeData.style"
      >
        {{ badgeData.text }}
      </span>
    </div>

    <div class="flex justify-between items-center mt-1">
      <span
        v-if="event.subject"
        class="font-bold text-gray-900 truncate pr-2"
        :class="[
          isList ? 'text-xl' : 'text-[13px]',
          { 'line-through text-gray-400': event.isCompleted }
        ]"
      >
        {{ event.title }}
      </span>

      <div v-else-if="!isList && event.location" class="flex items-center text-gray-500 text-[11px]">
        <svg class="w-3 h-3 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path></svg>
        {{ event.location }}
      </div>

      <input
        v-if="isDevoir"
        type="checkbox"
        class="rounded border-2 border-gray-400 text-primary focus:ring-primary shrink-0 cursor-pointer transition-all"
        :class="isList ? 'w-6 h-6' : 'w-4 h-4'"
        :checked="event.isCompleted"
        @change="emit('toggle-complete', event.id, ($event.target as HTMLInputElement).checked)"
      />
    </div>

    <div class="flex justify-between items-center mt-auto" :class="isList ? 'pt-4' : 'pt-2'">
      <div class="flex items-center text-gray-500 truncate pr-2" :class="isList ? 'text-sm' : 'text-[11px]'">
        <svg class="shrink-0 mr-1.5" :class="isList ? 'w-4 h-4' : 'w-3 h-3'" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
        <span v-if="isList">{{ event.date }}</span>
        <span v-else>{{ event.startTime }} - {{ event.endTime }}</span>
      </div>

      <span class="bg-gray-100 text-gray-700 px-2 py-0.5 rounded-full font-medium shrink-0" :class="isList ? 'text-xs' : 'text-[9px]'">
        {{ event.group }}
      </span>
    </div>
  </div>
</template>
