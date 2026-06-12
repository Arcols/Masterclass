<script setup lang="ts">
import { ref } from 'vue'
import Header from '@/components/Header.vue'
import PlanningBoard from '@/components/PlanningBoard.vue'
import EventDetailModal from '@/components/EventDetailModal.vue'
import type { EventData } from '@/components/EventCard.vue'

// L'état qui gère l'ouverture de la modale
const selectedEvent = ref<EventData | null>(null)

const handleUpdateStatus = (id: string, newValue: boolean) => {
  // si la modale est ouverte, on met à jour en direct
  if (selectedEvent.value && selectedEvent.value.id === id) {
    selectedEvent.value.isCompleted = newValue
  }
}

const handleDelete = (id: string) => {
  console.log("Supprimer l'event", id)
  selectedEvent.value = null
}

const handleEdit = (event: EventData) => {
  console.log("Éditer l'event", event)
  selectedEvent.value = null
}
</script>

<template>
  <div class="w-full h-screen flex flex-col bg-[var(--color-background)] overflow-hidden">
    <Header
      class="z-20 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="true"
      :show-profile="true"
      subtitle="FIL A1 2028"
    />

    <main class="flex-1 flex flex-col p-0 md:p-6 lg:p-8 min-h-0">
      <PlanningBoard class="flex-1 min-h-0" @open-details="(evt) => (selectedEvent = evt)" />
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
