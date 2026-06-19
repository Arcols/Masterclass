<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { CheckCircleIcon, ChevronDownIcon } from '@heroicons/vue/24/outline';
import EventCard, { type EventData } from '@/components/event/EventCard.vue';
import { getTodoList } from '@/services/timelineService';
import { useFilters } from '@/composables/useFilters';

const emit = defineEmits<{
  (e: 'open-details', event: EventData): void;
  (e: 'toggle-complete', id: string, newValue: boolean): void; // Ajouté pour synchroniser
}>();

const { selectedTypes, selectedGroups, showFavoritesOnly } = useFilters();

// Remplacement du mock par un tableau vide au démarrage
const localEvents = ref<EventData[]>([]);
const isLoading = ref(true);

// Chargement des données au montage du composant
onMounted(async () => {
  try {
    localEvents.value = await getTodoList();
  } catch (error) {
    console.error("Erreur lors du chargement de la todo list :", error);
  } finally {
    isLoading.value = false;
  }
});

const upcomingTasks = computed(() => {
  return localEvents.value
    // Règle 1 : timeline des devoirs/examens
    .filter(e => e.type === 'devoir' || e.type === 'examen')
    // Règle 2 : application des filtres globaux de la session
    .filter(e => {
      const isTypeMatched = selectedTypes.value.length === 0 || selectedTypes.value.includes(e.type);
      const isGroupMatched = selectedGroups.value.length === 0 || selectedGroups.value.includes(e.group);
      const isFavoriteMatched = !showFavoritesOnly.value || e.isFavorite;

      return isTypeMatched && isGroupMatched && isFavoriteMatched;
    })
    .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
});

const itemsPerPage = 4;
const visibleCount = ref(itemsPerPage);

const displayedTasks = computed(() => {
  return upcomingTasks.value.slice(0, visibleCount.value);
});

const loadMore = () => {
  visibleCount.value += itemsPerPage;
};

const handleToggleComplete = (id: string, newValue: boolean) => {
  const task = localEvents.value.find(e => e.id === id);
  if (task) task.isCompleted = newValue;

  // On prévient le parent (HomeView) si besoin de synchronisation
  emit('toggle-complete', id, newValue);
};
</script>

<template>
  <aside class="hidden lg:flex flex-col h-full w-80 xl:w-96 bg-white border border-gray-200 rounded-xl shadow-sm shrink-0 overflow-hidden">

    <div class="p-4 border-b border-gray-100 flex items-center gap-3 shrink-0 bg-white z-10">
      <CheckCircleIcon class="w-6 h-6 text-gray-500 shrink-0" />
      <h2 class="font-bold text-[var(--color-black)] whitespace-nowrap text-lg">
        Devoirs/Examens
      </h2>
    </div>

    <div class="flex-1 overflow-y-auto p-4 flex flex-col items-center bg-gray-50/30">
      <div class="mb-4 z-10 relative">
        <span class="bg-[var(--color-primary)] text-white text-[10px] font-bold px-3 py-1 rounded-full uppercase tracking-widest shadow-sm">
          Prochainement
        </span>
      </div>

      <div class="w-full flex flex-col gap-3">
        <div v-if="isLoading" class="flex flex-col gap-3 w-full">
          <div v-for="i in itemsPerPage" :key="'skeleton-'+i" class="h-28 bg-gray-200 animate-pulse rounded-xl w-full border border-gray-100"></div>
        </div>

        <template v-else>
          <div v-for="task in displayedTasks" :key="task.id" class="min-h-[110px]">
            <EventCard
              :event="task"
              layout="list"
              @open-details="emit('open-details', $event)"
              @toggle-complete="handleToggleComplete"
            />
          </div>

          <div v-if="displayedTasks.length === 0" class="text-center text-gray-500 py-6 text-sm">
            Aucun devoir ou examen à venir.
          </div>
        </template>
      </div>

      <button
        v-if="!isLoading && visibleCount < upcomingTasks.length"
        @click="loadMore"
        class="mt-6 w-full flex items-center justify-center gap-2 px-4 py-2.5 rounded-md bg-[var(--color-primary)] text-white text-sm font-medium transition hover:opacity-90 shadow-sm cursor-pointer"
      >
        Voir plus
        <ChevronDownIcon class="w-4 h-4" />
      </button>
    </div>
  </aside>
</template>
