<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline';
import type { EventType } from '@/types/event.ts';

const props = defineProps<{
  selectedTypes: EventType[];
  selectedGroups: string[];
  availableGroups: string[];
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'update:selectedTypes', types: EventType[]): void;
  (e: 'update:selectedGroups', groups: string[]): void;
}>();

// ── TYPES D'ÉVÉNEMENTS DISPONIBLES ──
const eventTypes: { value: EventType; label: string; color: string }[] = [
  { value: 'devoir', label: 'Devoirs', color: 'var(--color-tag-devoir-border)' },
  { value: 'examen', label: 'Examens', color: 'var(--color-tag-examen-border)' },
  { value: 'activite', label: 'Activités', color: 'var(--color-tag-activite-border)' },
  { value: 'sport', label: 'Sport', color: 'var(--color-tag-sport-border)' },
];

// ── GESTION DE LA SÉLECTION ──
const toggleType = (type: EventType) => {
  const current = [...props.selectedTypes];
  const index = current.indexOf(type);
  if (index > -1) current.splice(index, 1);
  else current.push(type);
  emit('update:selectedTypes', current);
};

const toggleGroup = (group: string) => {
  const current = [...props.selectedGroups];
  const index = current.indexOf(group);
  if (index > -1) current.splice(index, 1);
  else current.push(group);
  emit('update:selectedGroups', current);
};

// Fermeture avec Échap
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape') emit('close');
};

onMounted(() => document.addEventListener('keydown', handleKeydown));
onUnmounted(() => document.removeEventListener('keydown', handleKeydown));
</script>

<template>
  <div
    class="fixed inset-0 z-[999] flex items-center justify-center bg-[var(--color-black)]/40 backdrop-blur-sm p-4"
    @click.self="emit('close')"
  >
    <div class="bg-white rounded-xl shadow-xl w-full max-w-md overflow-hidden flex flex-col animate-fade-in-up p-0 m-0 border-none relative">

      <div class="flex items-center justify-between px-6 py-4 border-b border-gray-100 shrink-0">
        <h2 class="text-lg font-bold text-[var(--color-black)]">Filtres</h2>
        <button
          @click="emit('close')"
          class="p-1.5 rounded-full text-gray-400 hover:bg-gray-100 hover:text-gray-700 transition-colors cursor-pointer"
        >
          <XMarkIcon class="w-6 h-6" />
        </button>
      </div>

      <div class="p-6 overflow-y-auto max-h-[70vh] flex flex-col gap-6">

        <!-- Types d'événements -->
        <div>
          <h3 class="text-sm font-semibold text-gray-800 mb-3">Type d'événement</h3>
          <div class="flex flex-wrap gap-2">
            <button
              v-for="type in eventTypes"
              :key="type.value"
              @click="toggleType(type.value)"
              class="px-4 py-2 rounded-lg text-sm font-medium border-2 transition-all cursor-pointer"
              :class="
                selectedTypes.includes(type.value)
                  ? 'border-[var(--color-primary)] bg-[var(--color-primary)]/10 text-[var(--color-primary)]'
                  : 'border-gray-200 bg-white text-gray-600 hover:border-gray-300'
              "
            >
              {{ type.label }}
            </button>
          </div>
        </div>

        <!-- Groupes -->
        <div v-if="availableGroups.length > 0">
          <h3 class="text-sm font-semibold text-gray-800 mb-3">Groupes</h3>
          <div class="flex flex-wrap gap-2">
            <button
              v-for="group in availableGroups"
              :key="group"
              @click="toggleGroup(group)"
              class="px-4 py-2 rounded-lg text-sm font-medium border-2 transition-all cursor-pointer"
              :class="
                selectedGroups.includes(group)
                  ? 'border-[var(--color-primary)] bg-[var(--color-primary)]/10 text-[var(--color-primary)]'
                  : 'border-gray-200 bg-white text-gray-600 hover:border-gray-300'
              "
            >
              {{ group }}
            </button>
          </div>
        </div>

      </div>

      <div class="p-4 border-t border-gray-100 bg-gray-50 flex justify-end shrink-0">
        <button
          @click="emit('close')"
          class="px-5 py-2.5 bg-[var(--color-primary)] text-white rounded-md text-sm font-medium hover:opacity-90 transition-opacity cursor-pointer"
        >
          Afficher les résultats
        </button>
      </div>
    </div>
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
