<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline';
import type { EventType } from '@/types/event.ts';

const props = defineProps<{
  selectedTypes: EventType[];
  selectedGroups: string[];
  availableGroups: string[];
  showFavoritesOnly: boolean;
}>();

const emit = defineEmits<{
  (e: 'close'): void;
  (e: 'update:selectedTypes', types: EventType[]): void;
  (e: 'update:selectedGroups', groups: string[]): void;
  (e: 'update:showFavoritesOnly', value: boolean): void;
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
    <div class="bg-white rounded-xl shadow-xl w-full max-w-md max-h-[90vh] overflow-hidden flex flex-col animate-fade-in-up p-0 m-0 border-none relative">

      <!-- HEADER FIXE -->
      <div class="shrink-0 flex items-center justify-between px-6 py-4 border-b border-gray-100">
        <h2 class="text-lg font-bold text-[var(--color-black)]">Filtres</h2>
        <button
          @click="emit('close')"
          class="p-1.5 rounded-full text-gray-400 hover:bg-gray-100 hover:text-gray-700 transition-colors cursor-pointer"
        >
          <XMarkIcon class="w-6 h-6" />
        </button>
      </div>

      <!-- CONTENU SCROLLABLE -->
      <div class="flex-1 overflow-y-auto p-6 flex flex-col gap-6">

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

        <!-- Favoris -->
        <div class="flex items-center justify-between p-3 rounded-lg border border-gray-200 bg-gray-50">
          <div class="flex items-center gap-2">
            <svg class="w-5 h-5 text-[var(--color-event-favorite-selected)]" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
              <path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.006 5.404.434c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.434 2.082-5.005Z" clip-rule="evenodd" />
            </svg>
            <span class="text-sm font-semibold text-gray-800">Afficher uniquement les favoris</span>
          </div>

          <label class="relative inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              class="sr-only peer"
              :checked="showFavoritesOnly"
              @change="emit('update:showFavoritesOnly', ($event.target as HTMLInputElement).checked)"
            >
            <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[var(--color-event-favorite-selected)]"></div>
          </label>
        </div>
      </div>

      <!-- 4. FOOTER FIXE -->
      <div class="shrink-0 p-4 border-t border-gray-100 bg-gray-50 flex justify-end">
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
