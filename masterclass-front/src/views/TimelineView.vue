<script setup lang="ts">
import { ref, computed } from 'vue'
import Header from '@/components/Header.vue'
import EventCard, { type EventData } from '@/components/event/EventCard.vue'
import EventDetailModal from '@/components/modals/EventDetailModal.vue'
import mockEvents from '@/mocks/events.json'
import { onMounted } from 'vue'
import { useAuth } from '@/utils/checkingAuth'

const { requireAuth } = useAuth()

onMounted(async () => {
  await requireAuth() // ✅ redirige vers /login si token invalide
})

// On utilise les mêmes données que le panneau de bureau
const localEvents = ref<EventData[]>([...mockEvents] as EventData[])

const upcomingTasks = computed(() => {
  return localEvents.value
    .filter((e) => e.type === 'devoir' || e.type === 'examen')
    .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
})

// État pour la modale de détails
const selectedEvent = ref<EventData | null>(null)

const handleUpdateStatus = (id: string, newValue: boolean) => {
  const task = localEvents.value.find((e) => e.id === id)
  if (task) task.isCompleted = newValue
}

const handleDelete = (id: string) => {
  localEvents.value = localEvents.value.filter((e) => e.id !== id)
  selectedEvent.value = null
}
</script>

<template>
  <div class="w-full h-screen flex flex-col bg-[var(--color-background)] overflow-hidden relative">
    <Header
      class="relative z-[999] bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="false"
      :show-profile="true"
      subtitle="FIL A1 2028"
    />

    <main class="flex-1 overflow-y-auto p-4 md:p-6 lg:p-8">
      <div class="max-w-3xl mx-auto w-full">
        <div class="flex items-center justify-between mb-6">
          <h1 class="text-2xl font-bold text-[var(--color-black)]">Timeline Devoirs/Examens</h1>
          <span
            class="bg-[var(--color-primary)] text-white text-[10px] font-bold px-3 py-1 rounded-full uppercase tracking-widest shadow-sm"
          >
            Prochainement
          </span>
        </div>

        <div class="flex flex-col gap-3">
          <EventCard
            v-for="task in upcomingTasks"
            :key="task.id"
            :event="task"
            layout="list"
            @open-details="selectedEvent = task"
            @toggle-complete="handleUpdateStatus"
          />

          <div
            v-if="upcomingTasks.length === 0"
            class="text-center text-gray-500 py-12 bg-white rounded-xl border border-gray-100"
          >
            Aucun devoir ou examen à venir. Profites-en pour te reposer !
          </div>
        </div>
      </div>
    </main>

    <EventDetailModal
      v-if="selectedEvent"
      :event="selectedEvent"
      @close="selectedEvent = null"
      @toggle-complete="(id, val) => handleUpdateStatus(id, val)"
      @delete="handleDelete"
    />
  </div>
</template>
