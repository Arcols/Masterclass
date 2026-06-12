<style scoped>
</style>
<script setup lang="ts">
import { ref } from 'vue';
import EventCard from '@/components/EventCard.vue';
import jsonMock from '@/mocks/events.json';
import Header from '@/components/Header.vue'
import { RouterView } from 'vue-router';
import { RouterView } from 'vue-router';

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
    <Header
      :show-actions="true"
      :show-profile="true"
      subtitle="FIL A1 2028"
    />
    <RouterView />
</template>
