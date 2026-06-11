<style scoped>
</style>
<script setup lang="ts">
import { ref } from 'vue';
import EventCard from '@/components/EventCard.vue';
import jsonMock from '@/mocks/events.json';

// On rend le mock réactif pour pouvoir tester les clics sur la checkbox
const events = ref(jsonMock);

// Met à jour l'état de l'événement lors du clic
const updateStatus = (id: string, newValue: boolean) => {
  const targetEvent = events.value.find(e => e.id === id);
  if (targetEvent) {
    targetEvent.isCompleted = newValue;
  }
};
</script>

<template>
  <div class="p-10 flex gap-10 bg-gray-50 min-h-screen">
    <div class="w-1/2 flex flex-col gap-3">
      <h2 class="font-bold mb-4 text-xl">Format Liste (Dashboard)</h2>
      <EventCard
        v-for="event in events"
        :key="'list-' + event.id"
        :event="event"
        layout="list"
        @toggle-complete="updateStatus"
      />
    </div>

    <div class="w-72 flex flex-col gap-3">
      <h2 class="font-bold mb-4 text-xl">Format Calendrier (Planning)</h2>
      <EventCard
        v-for="event in events"
        :key="'cal-' + event.id"
        :event="event"
        layout="calendar"
        @toggle-complete="updateStatus"
      />
    </div>
  </div>
</template>
