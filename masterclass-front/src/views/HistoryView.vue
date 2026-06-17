<script setup lang="ts">
import { ref, computed } from 'vue';
import { ChevronDownIcon, FunnelIcon } from '@heroicons/vue/24/outline';
import Header from '@/components/Header.vue';
import EventCard, { type EventData } from '@/components/event/EventCard.vue';
import EventDetailModal from '@/components/modals/EventDetailModal.vue';
import FilterModal from '@/components/modals/FilterModal.vue';
import mockEvents from '@/mocks/events.json';
import type { EventType } from '@/types/event.ts';

// ── DONNÉES ──
const allEvents = ref<EventData[]>(mockEvents as EventData[]);

// ── FILTRES (ÉTAT LOCAL, ISOLÉ DE L'ACCUEIL) ──
const selectedTypes = ref<EventType[]>([]);
const selectedGroups = ref<string[]>([]);
const showFavoritesOnly = ref(false);
const isFilterModalOpen = ref(false);

const activeFilterCount = computed(() => {
  return selectedTypes.value.length + selectedGroups.value.length + (showFavoritesOnly.value ? 1 : 0);
});

const resetFilters = () => {
  selectedTypes.value = [];
  selectedGroups.value = [];
  showFavoritesOnly.value = false;
};

const availableGroups = computed(() => {
  const groups = allEvents.value.map(e => e.group);
  return [...new Set(groups)];
});

const filteredEvents = computed(() => {
  let filtered = allEvents.value.filter(e => {
    const matchType = selectedTypes.value.length === 0 || selectedTypes.value.includes(e.type as EventType);
    const matchGroup = selectedGroups.value.length === 0 || selectedGroups.value.includes(e.group);
    const matchFavorite = !showFavoritesOnly.value || e.isFavorite;

    return matchType && matchGroup && matchFavorite;
  });

  // Tri par date décroissante (du plus récent au plus ancien)
  return filtered.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
});

// ── PAGINATION (VOIR PLUS) ──
const itemsPerPage = 5;
const visibleCount = ref(itemsPerPage);

const displayedEvents = computed(() => {
  return filteredEvents.value.slice(0, visibleCount.value);
});

const loadMore = () => {
  visibleCount.value += itemsPerPage;
};

// ── MODALE DÉTAILS & ACTIONS ──
const selectedEvent = ref<EventData | null>(null);

const handleUpdateStatus = (id: string, newValue: boolean) => {
  const target = allEvents.value.find(e => e.id === id);
  if (target) target.isCompleted = newValue;
  if (selectedEvent.value && selectedEvent.value.id === id) {
    selectedEvent.value.isCompleted = newValue;
  }
};

const handleDelete = (id: string) => {
  allEvents.value = allEvents.value.filter(e => e.id !== id);
  selectedEvent.value = null;
};

const handleEdit = (event: EventData) => {
  console.log('Éditer', event);
  selectedEvent.value = null;
};
</script>

<template>
  <div class="w-full min-h-screen flex flex-col bg-[var(--color-background)]">

    <Header
      class="z-50 bg-[var(--color-background)] border-b border-gray-100 shadow-sm shrink-0"
      :show-actions="false"
      :show-profile="true"
      subtitle="FIL A1 2028"
    />

    <main class="flex-1 flex flex-col items-center py-6 md:py-10 px-4 overflow-y-auto">

      <div class="w-full max-w-2xl flex flex-col gap-6">

        <div class="flex items-start md:items-center justify-between gap-4 border-b border-gray-200 pb-4">
          <h1 class="text-xl md:text-3xl font-bold text-[var(--color-black)] leading-tight">
            Historique
          </h1>

          <div class="flex items-center gap-1.5 shrink-0">
            <button
              @click="isFilterModalOpen = true"
              class="relative flex items-center justify-center gap-2 p-2 md:px-3 md:py-1.5 bg-[var(--color-primary)] text-white rounded-md text-sm font-medium hover:opacity-90 transition-opacity cursor-pointer"
            >
              <FunnelIcon class="w-4 h-4 md:w-4 md:h-4" />
              <span class="hidden md:inline">Filtres</span>

              <span
                v-if="activeFilterCount > 0"
                class="absolute -top-2 -right-2 flex items-center justify-center min-w-[20px] h-5 px-1 text-[11px] font-bold text-white bg-[var(--color-red)] rounded-full border-2 border-white shadow-sm"
              >
                {{ activeFilterCount }}
              </span>
            </button>

            <button
              v-if="activeFilterCount > 0"
              @click="resetFilters"
              class="flex items-center justify-center p-2 md:p-1.5 border-2 border-[var(--color-primary)] text-[var(--color-primary)] rounded-md hover:bg-[var(--color-primary)]/10 transition-colors cursor-pointer"
              title="Effacer les filtres"
            >
              <svg class="w-4 h-4 md:w-[18px] md:h-[18px]" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                <path stroke-linecap="round" stroke-linejoin="round" d="M3 3l18 18" />
              </svg>
            </button>
          </div>
        </div>

        <div class="flex flex-col gap-3">
          <EventCard
            v-for="event in displayedEvents"
            :key="event.id"
            :event="event"
            layout="list"
            @open-details="selectedEvent = $event"
            @toggle-complete="handleUpdateStatus"
          />

          <div v-if="displayedEvents.length === 0" class="text-center text-gray-500 py-12 bg-white rounded-xl border border-gray-100 shadow-sm">
            Aucun événement ne correspond à vos filtres.
          </div>
        </div>

        <button
          v-if="visibleCount < filteredEvents.length"
          @click="loadMore"
          class="mx-auto mt-4 flex items-center justify-center gap-2 px-6 py-2.5 rounded-md border border-gray-300 bg-white text-[var(--color-black)] text-sm font-medium transition hover:bg-gray-50 cursor-pointer w-full md:w-auto shadow-sm"
        >
          Voir plus
          <ChevronDownIcon class="w-4 h-4" />
        </button>

      </div>
    </main>

    <EventDetailModal
      v-if="selectedEvent"
      :event="selectedEvent"
      @close="selectedEvent = null"
      @toggle-complete="handleUpdateStatus"
      @delete="handleDelete"
      @edit="handleEdit"
    />

    <FilterModal
      v-if="isFilterModalOpen"
      :selected-types="selectedTypes"
      :selected-groups="selectedGroups"
      :available-groups="availableGroups"
      :show-favorites-only="showFavoritesOnly"
      @update:selected-types="selectedTypes = $event"
      @update:selected-groups="selectedGroups = $event"
      @update:show-favorites-only="showFavoritesOnly = $event"
      @close="isFilterModalOpen = false"
    />
  </div>
</template>
