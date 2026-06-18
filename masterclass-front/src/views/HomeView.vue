<script setup lang="ts">
import { ref } from 'vue'
import Header from '@/components/Header.vue'
import AddEventModal from '@/components/modals/AddEventModal.vue'
import PlanningBoard from '@/components/planning/PlanningBoard.vue'
import EventDetailModal from '@/components/modals/EventDetailModal.vue'
import type { EventData } from '@/components/event/EventCard.vue'
import {
  makeOnAddEvent,
  makeOnRequestAdd,
  makeHandleUpdateStatus,
  makeHandleDelete,
  makeHandleEdit,
} from '@/utils/appHandlers'
import TodoListPanel from '@/components/TodoListPanel.vue'
import { onMounted } from 'vue'
import { useAuth } from '@/utils/checkingAuth'
import GlobalFilterAlert from '@/components/GlobalFilterAlert.vue'

const { requireAuth } = useAuth()

onMounted(async () => {
  await requireAuth() // redirige vers /login si token invalide
})

const addEventRef = ref<InstanceType<typeof AddEventModal> | null>(null)

const onAddEvent = makeOnAddEvent(addEventRef)
const onRequestAdd = makeOnRequestAdd(addEventRef)

const selectedEvent = ref<EventData | null>(null)

const isTodoListOpen = ref(true) // Ouvert par défaut sur grand écran

const handleUpdateStatus = makeHandleUpdateStatus(selectedEvent)
const handleDelete = makeHandleDelete(selectedEvent)
const handleEditBase = makeHandleEdit(addEventRef)
const handleEdit = (event: EventData) => {
  selectedEvent.value = null
  handleEditBase(event)
}
</script>

<template>
  <div class="w-full h-screen flex flex-col bg-[var(--color-background)] overflow-hidden relative">
    <Header
      class="z-100 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="true"
      :show-profile="true"
      subtitle="FIL A1 2028"
      @add-event="onAddEvent"
    />

    <!-- bandeau d'alerte global -->
    <GlobalFilterAlert />

    <main class="flex-1 flex gap-4 md:p-2 lg:p-4 min-h-0 overflow-hidden">
      <PlanningBoard
        class="flex-1 min-w-0 min-h-0"
        :is-sidebar-open="isTodoListOpen"
        @toggle-sidebar="isTodoListOpen = !isTodoListOpen"
        @open-details="(evt) => (selectedEvent = evt)"
        @request-add="(p) => onRequestAdd(p)"
      />

      <TodoListPanel v-if="isTodoListOpen" @open-details="(evt) => (selectedEvent = evt)" />
    </main>

    <EventDetailModal
      v-if="selectedEvent"
      :event="selectedEvent"
      @close="selectedEvent = null"
      @toggle-complete="handleUpdateStatus"
      @delete="handleDelete"
      @edit="handleEdit"
    />

    <button
      @click="onAddEvent"
      class="md:hidden fixed bottom-6 right-6 z-[90] w-11 h-11 bg-[var(--color-primary)] text-white rounded-full shadow-xl flex items-center justify-center hover:scale-105 active:scale-95 transition-transform focus:outline-none cursor-pointer"
      aria-label="Ajouter un événement"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="w-7 h-7"
        viewBox="0 0 256 256"
        fill="currentColor"
      >
        <path
          d="M228,128a12,12,0,0,1-12,12H140v76a12,12,0,0,1-24,0V140H40a12,12,0,0,1,0-24h76V40a12,12,0,0,1,24,0v76h76A12,12,0,0,1,228,128Z"
        />
      </svg>
    </button>
  </div>

  <AddEventModal ref="addEventRef" />
</template>
