<script setup lang="ts">
import { ref, computed } from 'vue';
import { ChevronDownIcon } from '@heroicons/vue/24/outline';
import Header from '@/components/Header.vue';
import EventCard, { type EventData } from '@/components/event/EventCard.vue';
import EventDetailModal from '@/components/event/EventDetailModal.vue';
import mockEvents from '@/mocks/events.json';
import type { EventType } from '@/types/event.ts';
import PlanningFilters from '@/components/PlanningFilters.vue'

// ── DONNÉES ──
const allEvents = ref<EventData[]>(mockEvents as EventData[]);

// ── FILTRES ──
const selectedTypes = ref<EventType[]>([]);
const selectedGroups = ref<string[]>([]);

const availableGroups = computed(() => {
  const groups = allEvents.value.map(e => e.group);
  return [...new Set(groups)];
});

const filteredEvents = computed(() => {
  let filtered = allEvents.value.filter(e => {
    const matchType = selectedTypes.value.length === 0 || selectedTypes.value.includes(e.type as EventType);
    const matchGroup = selectedGroups.value.length === 0 || selectedGroups.value.includes(e.group);
    return matchType && matchGroup;
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

// ── MODALE & ACTIONS ──
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
  <div class="w-full min-h-screen flex flex-col bg-gray-50/50">

    <Header
      class="z-50 bg-white border-b border-gray-100 shadow-sm shrink-0"
      :show-actions="false"
      :show-profile="true"
      subtitle="FIL A1 2028"
    />

    <main class="flex-1 flex flex-col items-center py-10 px-4 overflow-y-auto">

      <h1 class="mb-8 text-3xl md:text-4xl font-bold text-[var(--color-black)] text-center">
        Historique des évènements
      </h1>

      <div class="flex justify-center w-full max-w-4xl mb-8">
        <PlanningFilters
          class="!bg-transparent !border-none !px-0 justify-center"
          v-model:selected-types="selectedTypes"
          v-model:selected-groups="selectedGroups"
          :available-groups="availableGroups"
        />
      </div>

      <div class="w-full max-w-2xl flex flex-col gap-4">

        <div
          v-for="event in displayedEvents"
          :key="event.id"
        >
          <EventCard
            :event="event"
            layout="list"
            @open-details="selectedEvent = $event"
            @toggle-complete="handleUpdateStatus"
          />
        </div>

        <div v-if="displayedEvents.length === 0" class="text-center text-gray-500 py-10">
          Aucun événement ne correspond à vos filtres.
        </div>
      </div>

      <button
        v-if="visibleCount < filteredEvents.length"
        @click="loadMore"
        class="mt-8 flex items-center gap-2 px-6 py-2.5 rounded-md bg-[var(--color-primary)] text-white text-sm font-medium transition hover:opacity-90"
      >
        Voir plus
        <ChevronDownIcon class="w-4 h-4" />
      </button>

    </main>

    <EventDetailModal
      v-if="selectedEvent"
      :event="selectedEvent"
      @close="selectedEvent = null"
      @toggle-complete="handleUpdateStatus"
      @delete="handleDelete"
      @edit="handleEdit"
    />

  </div>
</template>
