<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import AddEventModal from '@/components/modals/AddEventModal.vue'
import PlanningBoard from '@/components/planning/PlanningBoard.vue'
import EventDetailModal from '@/components/modals/EventDetailModal.vue'
import type { EventData } from '@/components/event/EventCard.vue'
import { makeOnAddEvent, makeOnRequestAdd, makeHandleUpdateStatus, makeHandleDelete, makeHandleEdit } from '@/utils/appHandlers'
import TodoListPanel from '@/components/TodoListPanel.vue'

const router = useRouter()
const addEventRef = ref<InstanceType<typeof AddEventModal> | null>(null)

const onAddEvent = makeOnAddEvent(addEventRef)
const onRequestAdd = makeOnRequestAdd(addEventRef)

const selectedEvent = ref<EventData | null>(null);

// ── NOUVEL ÉTAT POUR LE PANNEAU DE DROITE ──
const isTodoListOpen = ref(true); // Ouvert par défaut sur grand écran

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
      class="z-50 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="true"
      :show-profile="true"
      subtitle="FIL A1 2028"
      @add-event="onAddEvent"
      @open-history="router.push('/history')"
    />

    <main class="flex-1 flex gap-6 p-4 md:p-6 lg:p-8 min-h-0 overflow-hidden">
      <PlanningBoard
        class="flex-1 min-w-0 min-h-0"
        :is-sidebar-open="isTodoListOpen"
        @toggle-sidebar="isTodoListOpen = !isTodoListOpen"
        @open-details="(evt) => selectedEvent = evt"
        @request-add="(p) => onRequestAdd(p)"
      />

      <TodoListPanel
        v-if="isTodoListOpen"
        @open-details="(evt) => selectedEvent = evt"
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
