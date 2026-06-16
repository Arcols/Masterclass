<script setup lang="ts">
import { ref } from 'vue'
import Header from '@/components/Header.vue'
import AddEventModal from '@/components/modals/AddEventModal.vue'
import PlanningBoard from '@/components/planning/PlanningBoard.vue'
import EventDetailModal from '@/components/modals/EventDetailModal.vue'
import type { EventData } from '@/components/event/EventCard.vue'
import { makeOnAddEvent, makeOnRequestAdd, makeHandleUpdateStatus, makeHandleDelete, makeHandleEdit } from '@/utils/appHandlers'
import { useRouter } from 'vue-router'

const router = useRouter()
const addEventRef = ref<InstanceType<typeof AddEventModal> | null>(null)

const onAddEvent = makeOnAddEvent(addEventRef)
const onRequestAdd = makeOnRequestAdd(addEventRef)

// L'état qui gère l'ouverture de la modale
const selectedEvent = ref<EventData | null>(null);

const handleUpdateStatus = makeHandleUpdateStatus(selectedEvent)
const handleDelete = makeHandleDelete(selectedEvent)
const handleEditBase = makeHandleEdit(addEventRef)
const handleEdit = (event: EventData) => {
  selectedEvent.value = null
  handleEditBase(event)
}
</script>

<template>
  <div class="w-full h-screen flex flex-col bg-[var(--color-background)] overflow-hidden">

    <Header
      class="z-100 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="true"
      :show-profile="true"
      subtitle="FIL A1 2028"
      @add-event="onAddEvent"
      @open-history="router.push('/history')"
    />

    <main class="flex-1 flex flex-col p-0 md:p-6 lg:p-8 min-h-0">
      <PlanningBoard
        class="flex-1 min-h-0"
        @open-details="(evt) => selectedEvent = evt"
        @request-add="(p) => onRequestAdd(p)"
      />
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

  <AddEventModal ref="addEventRef" />
</template>
