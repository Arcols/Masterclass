<script setup lang="ts">
import { computed } from 'vue';
import {
  XMarkIcon,
  CalendarIcon,
  ClockIcon,
  UserGroupIcon,
  MapPinIcon,
  TrashIcon,
  PencilIcon,
  CheckCircleIcon,
  CheckIcon
} from '@heroicons/vue/24/outline';
import EventBadge from '../event/EventBadge.vue';
import PriorityIndicator from '../event/PriorityIndicator.vue';
import type { EventData } from '../event/EventCard.vue';

const props = defineProps<{
  event: EventData;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'delete', id: string): void;
  (e: 'edit', event: EventData): void;
  (e: 'toggle-complete', id: string, newValue: boolean): void;
}>();

const isDevoir = computed(() => props.event.type === 'devoir');

// ── FORMATAGE DE LA DATE ──
const formattedDate = computed(() => {
  if (!props.event.date) return 'Date non définie';

  const d = new Date(`${props.event.date}T00:00:00`);
  const options: Intl.DateTimeFormatOptions = { weekday: 'long', day: 'numeric', month: 'long' };
  const dateStr = new Intl.DateTimeFormat('fr-FR', options).format(d);

  // Met la première lettre en majuscule
  const capitalizedDate = dateStr.charAt(0).toUpperCase() + dateStr.slice(1);

  if (isDevoir.value) {
    return `A rendre pour le ${dateStr} / ${props.event.startTime} - ${props.event.endTime}`;
  }
  return `${capitalizedDate} / ${props.event.startTime} - ${props.event.endTime}`;
});

const modalTitle = computed(() => {
  if (props.event.type === 'devoir') return 'Détails du devoir';
  if (props.event.type === 'sport') return "Détails de l'évènement sportif";
  return "Détails de l'activité";
});
</script>

<template>
  <div
    class="fixed inset-0 z-50 flex items-center justify-center bg-[var(--color-black)]/40 backdrop-blur-sm p-4"
    @click.self="emit('close')"
  >
    <dialog
      open
      class="bg-white rounded-xl shadow-xl w-full max-w-lg overflow-hidden flex flex-col animate-fade-in-up p-0 m-0 border-none relative"
      aria-labelledby="event-detail-title"
    >

      <div class="flex items-center justify-between px-6 py-4 border-b border-gray-100">
        <h2 id="event-detail-title" class="text-lg font-bold text-[var(--color-black)]">{{ modalTitle }}</h2>
        <button
          @click="emit('close')"
          class="p-1.5 rounded-full text-gray-400 hover:bg-gray-100 hover:text-gray-700 transition-colors"
          aria-label="Fermer la modale"
        >
          <XMarkIcon class="w-6 h-6" />
        </button>
      </div>

      <div class="p-6">

        <div class="flex items-center gap-4 mb-6">
          <div
            v-if="isDevoir"
            class="w-12 h-12 shrink-0 rounded-full border border-gray-200 flex items-center justify-center bg-gray-50"
          >
            <CheckCircleIcon class="w-7 h-7 text-gray-500" />
          </div>
          <div
            v-else
            class="w-12 h-12 shrink-0 rounded-lg flex items-center justify-center bg-[var(--color-primary)]/10 text-[var(--color-primary)]"
          >
            <CalendarIcon class="w-7 h-7" />
          </div>

          <div class="flex flex-col items-start gap-1">
            <div class="flex items-center gap-2">
              <EventBadge :type="event.type" />
              <PriorityIndicator v-if="event.priority" :priority="event.priority" />
            </div>

            <h3 class="text-xl font-bold text-[var(--color-black)]">{{ event.title }}</h3>

            <span v-if="event.subject" class="text-sm font-semibold text-gray-500">
              {{ event.subject }}
            </span>
          </div>
        </div>

        <hr class="border-gray-100 mb-6" />

        <div class="flex flex-col gap-3 mb-6">
          <div class="flex items-center text-sm" :class="isDevoir ? 'text-[var(--color-red)] font-medium' : 'text-gray-800'">
            <ClockIcon class="w-5 h-5 mr-3 shrink-0" />
            {{ formattedDate }}
          </div>

          <div class="flex items-center text-sm text-gray-800">
            <UserGroupIcon class="w-5 h-5 mr-3 shrink-0 text-gray-500" />
            Groupe : <span class="font-medium ml-1">{{ event.group }}</span>
          </div>

          <div v-if="event.location" class="flex items-center text-sm text-gray-800">
            <MapPinIcon class="w-5 h-5 mr-3 shrink-0 text-gray-500" />
            {{ event.location }}
          </div>
        </div>

        <div class="bg-gray-50 rounded-lg p-4 text-sm text-gray-600 border border-gray-100 mb-8">
          {{ event.description || "Aucune description pour cet événement." }}
        </div>

        <div class="flex items-center gap-3">
          <button
            v-if="isDevoir"
            @click="emit('toggle-complete', event.id, !event.isCompleted)"
            class="flex items-center justify-center gap-2 px-4 py-2 rounded-md font-medium text-sm transition-colors cursor-pointer"
            :class="event.isCompleted ? 'bg-gray-200 text-gray-700' : 'bg-[var(--color-primary)] text-white hover:opacity-90'"
          >
            <CheckIcon v-if="!event.isCompleted" class="w-4 h-4" />
            {{ event.isCompleted ? 'Marqué comme non fait' : 'Marqué comme fait' }}
          </button>

          <button
            @click="emit('edit', event)"
            class="flex items-center justify-center gap-2 px-4 py-2 rounded-md border border-gray-200 font-medium text-sm text-gray-700 hover:bg-gray-50 transition-colors cursor-pointer"
          >
            <PencilIcon class="w-4 h-4" />
            Modifier
          </button>

          <button
            @click="emit('delete', event.id)"
            class="flex items-center justify-center gap-2 px-4 py-2 rounded-md font-medium text-sm text-[var(--color-red)] bg-red-50 hover:bg-red-100 transition-colors cursor-pointer"
          >
            <TrashIcon class="w-4 h-4" />
            Supprimer
          </button>
        </div>

      </div>
    </dialog>
  </div>
</template>

<style scoped>
.animate-fade-in-up {
  animation: fadeInUp 0.2s ease-out forwards;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(10px) scale(0.98); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}
</style>
