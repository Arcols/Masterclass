<script setup lang="ts">
import { ref, onMounted } from 'vue'
import Header from '@/components/Header.vue'
import AddEventModal from '@/components/modals/AddEventModal.vue'
import PlanningBoard from '@/components/planning/PlanningBoard.vue'
import EventDetailModal from '@/components/modals/EventDetailModal.vue'
import type { EventData } from '@/components/event/EventCard.vue'
import {
  makeOnAddEvent,
  makeOnRequestAdd,
  makeHandleDelete,
  makeHandleEdit,
} from '@/utils/appHandlers'
import TodoListPanel from '@/components/TodoListPanel.vue'
import { useAuth } from '@/utils/checkingAuth'
import GlobalFilterAlert from '@/components/GlobalFilterAlert.vue'
import { usePlanningEvents } from '@/composables/usePlanningEvents'

const { requireAuth } = useAuth()
const { updateEventCompletionStatus } = usePlanningEvents()

onMounted(async () => {
  await requireAuth() // redirige vers /login si token invalide
})

const addEventRef = ref<InstanceType<typeof AddEventModal> | null>(null)
const todoListRef = ref<InstanceType<typeof TodoListPanel> | null>(null)

const onAddEvent = makeOnAddEvent(addEventRef)
const onRequestAdd = makeOnRequestAdd(addEventRef)

const selectedEvent = ref<EventData | null>(null)

const isTodoListOpen = ref(true)

// LE COEUR DE LA SYNCHRONISATION
const handleUpdateStatus = (id: string, newValue: boolean) => {
  // Maj de la Modale de détails
  if (selectedEvent.value && selectedEvent.value.id === id) {
    selectedEvent.value.isCompleted = newValue
  }

  // Maj du calendrier (PlanningBoard)
  updateEventCompletionStatus(id, newValue)

  // Maj du panneau latéral (TodoListPanel)
  if (todoListRef.value) {
    todoListRef.value.forceUpdateStatus(id, newValue)
  }
}

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
      :show-add-event-button="true"
      :show-profile="true"
      :show-navigation="true"
      subtitle="FIL A1 2028"
      @add-event="onAddEvent"
    />

    <GlobalFilterAlert />

    <main class="flex-1 flex gap-4 md:p-2 lg:p-4 min-h-0 overflow-hidden">
      <PlanningBoard
        class="flex-1 min-w-0 min-h-0"
        :is-sidebar-open="isTodoListOpen"
        @toggle-sidebar="isTodoListOpen = !isTodoListOpen"
        @open-details="(evt) => (selectedEvent = evt)"
        @request-add="(p) => onRequestAdd(p)"
        @toggle-complete="handleUpdateStatus"
      />

      <TodoListPanel
        v-if="isTodoListOpen"
        ref="todoListRef"
        @open-details="(evt) => (selectedEvent = evt)"
        @toggle-complete="handleUpdateStatus"
      />
    </main>

    <EventDetailModal
      v-if="selectedEvent"
      :event-id="selectedEvent.id"
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
      <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7" viewBox="0 0 256 256" fill="currentColor">
        <path d="M228,128a12,12,0,0,1-12,12H140v76a12,12,0,0,1-24,0V140H40a12,12,0,0,1,0-24h76V40a12,12,0,0,1,24,0v76h76A12,12,0,0,1,228,128Z" />
      </svg>
    </button>
  </div>

  <AddEventModal ref="addEventRef" />
</template>
