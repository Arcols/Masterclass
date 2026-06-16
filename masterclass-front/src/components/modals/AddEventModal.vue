<script setup lang="ts">
import { computed, onMounted, onBeforeUnmount, ref } from 'vue'
import { getClasses } from '@/services/classesService.ts'
import { createEvent,updateEvent } from '@/services/eventService.ts'
import type { Classe } from '@/types/classes.ts'
import type { EventType, EventPayload } from '@/types/events.ts'
import EventTypeSelector from '@/components/modals/addEventModal/EventTypeSelector.vue'
import EventBasicFields from '@/components/modals/addEventModal/EventBasicFields.vue'
import EventSubjectField from '@/components/modals/addEventModal/EventSubjectField.vue'
import EventDateLocationFields from '@/components/modals/addEventModal/EventDateLocationFields.vue'
import EventTimeFields from './addEventModal/EventTimeFields.vue'

const emit = defineEmits<{
  'event-saved': []
}>()

const props = withDefaults(
  defineProps<{
    eventToEdit?: EventPayload
  }>(),
  {
    eventToEdit: undefined,
  }
)

const dialogRef = ref<HTMLDialogElement | null>(null)
const isDialogVisible = ref<boolean>(false)
const classes = ref<Classe[]>([])
const selectedType = ref<EventType>(props.eventToEdit?.type ?? 'devoir')

const isEditMode = ref<boolean>(false)
const eventIdToEdit = ref<number | string | undefined>(undefined)

const title = ref<string>('')
const description = ref<string>('')
const group = ref<string>('')
const subject = ref<string>('')
const location = ref<string>('')
const date = ref<string>(getTodayDate())
const startTime = ref<string>('08:00')
const endTime = ref<string>('09:00')
const dueTime = ref<string>('08:00')

const typeLabels: Record<EventType, string> = {
  devoir: 'Devoir',
  examen: 'Examen',
  activite: 'Activité',
  sport: 'Sport',
}

const isExamen = computed<boolean>(() => selectedType.value === 'examen')
const isDevoir = computed<boolean>(() => selectedType.value === 'devoir')

const dialogTitle = computed<string>(() => {
  const feminineTypes: EventType[] = ['activite']
  const article = isEditMode.value ? 'Modifier' : (feminineTypes.includes(selectedType.value) ? 'Nouvelle' : 'Nouveau')
  const label = typeLabels[selectedType.value] ?? ''
  return `${article} ${label}`
})

onMounted(async (): Promise<void> => {
  classes.value = await getClasses()
  if (!subject.value && classes.value[0]?.value) {
    subject.value = classes.value[0].value
  }
  // add global key listener for Escape to close modal
  window.addEventListener('keydown', closeOnEsc)
})

onBeforeUnmount(() => {
  window.removeEventListener('keydown', closeOnEsc)
})

function getTodayDate(): string {
  const now = new Date()
  const localDate = new Date(now.getTime() - now.getTimezoneOffset() * 60000)
  return localDate.toISOString().slice(0, 10)
}

function resetForm(): void {
  selectedType.value = 'devoir'
  title.value = ''
  description.value = ''
  group.value = ''
  subject.value = classes.value[0]?.value ?? ''
  location.value = ''
  date.value = getTodayDate()
  startTime.value = '08:00'
  endTime.value = '09:00'
  dueTime.value = '08:00'
  isEditMode.value = false
  eventIdToEdit.value = undefined
}

function closeEventPopUp(): void {
   isDialogVisible.value = false
   dialogRef.value?.close()
 }

function closeOnEsc(event: KeyboardEvent): void {
  if (event.key === 'Escape' || event.key === 'Esc') {
    if (isDialogVisible.value) {
      closeEventPopUp()
    }
  }
}

function populateFormForEdit(eventToEdit: EventPayload) {
  isEditMode.value = true
  eventIdToEdit.value = eventToEdit.id
  selectedType.value = eventToEdit.type
  title.value = eventToEdit.title
  description.value = eventToEdit.description
  group.value = eventToEdit.group ?? ''
  subject.value = eventToEdit.subject ?? ''
  location.value = eventToEdit.location
  date.value = eventToEdit.date
  startTime.value = eventToEdit.startTime
  endTime.value = eventToEdit.endTime
  dueTime.value = eventToEdit.dueTime
}

function addEventPopup(eventToEdit?: EventPayload): void {
   resetForm()

   if (eventToEdit) {
     // If payload contains an id, open in edit mode, otherwise prefill for creation
     if (eventToEdit.id !== undefined && eventToEdit.id !== null) {
       populateFormForEdit(eventToEdit);
     } else {
       // Prefill fields without switching to edit mode
       selectedType.value = eventToEdit.type ?? selectedType.value
       title.value = eventToEdit.title ?? ''
       description.value = eventToEdit.description ?? ''
      group.value = eventToEdit.group ?? group.value
       subject.value = eventToEdit.subject ?? subject.value
       location.value = eventToEdit.location ?? ''
       date.value = eventToEdit.date ?? date.value
       startTime.value = eventToEdit.startTime ?? startTime.value
       endTime.value = eventToEdit.endTime ?? endTime.value
       dueTime.value = eventToEdit.dueTime ?? dueTime.value
     }
   }

   isDialogVisible.value = true
   dialogRef.value?.showModal()
 }

defineExpose({ addEventPopup })

async function submit(): Promise<void> {
  const start = Number(startTime.value.slice(0, 2)) * 60 + Number(startTime.value.slice(3, 5));
  const end = Number(endTime.value.slice(0, 2)) * 60 + Number(endTime.value.slice(3, 5));
  if (
    !isFieldValid(title.value) ||
    !isFieldValid(location.value) ||
    !isFieldValid(date.value) ||
    !isFieldValid(startTime.value) ||
    !isFieldValid(endTime.value) ||
    (isExamen.value && !isFieldValid(subject.value)) ||
    (isDevoir.value && !isFieldValid(subject.value)) ||
    (isDevoir.value && !isFieldValid(dueTime.value)) ||
    end <= start
  ) {
    return
  }

  const payload: EventPayload = {
    id: eventIdToEdit.value,
    type: selectedType.value,
    title: title.value,
    description: description.value,
    group: group.value || undefined,
    subject: isDevoir.value ? subject.value : undefined,
    location: location.value,
    date: date.value,
    startTime: startTime.value,
    endTime: endTime.value,
    dueTime: dueTime.value,
  }

  try {
    if (isEditMode.value) {
      await updateEvent(eventIdToEdit.value as number | string, payload)
    } else {
      await createEvent(payload)
    }
    emit('event-saved')
    closeEventPopUp()
  } catch (error) {
    console.error('Error saving event:', error)
  }
}

function isFieldValid(value: unknown): boolean {
  if (value === null || value === undefined) return false
  if (Array.isArray(value) && value.length === 0) return false
  return String(value).trim().length > 0
}
</script>

<template>
  <div v-if="isDialogVisible" class="fixed inset-0 z-50 flex items-start justify-center bg-[var(--color-black)]/40 backdrop-blur-sm p-4 pt-16" @click.self="closeEventPopUp()">
    <transition name="modal-fade">
      <dialog
        v-if="isDialogVisible"
        ref="dialogRef"
        open
        :class="['add-event-card', selectedType]"
        class="relative bg-white rounded-xl w-full max-w-3xl mx-0 my-6 p-4 sm:p-6 shadow-xl z-10 border-none animate-fade-in-up max-h-[calc(100dvh-7rem)] overflow-y-auto overscroll-contain"
        @keydown.esc.prevent="closeEventPopUp()"
      >
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-2xl font-bold">{{ dialogTitle }}</h3>
          <button
            type="button"
            class="text-3xl leading-none text-gray-500 cursor-pointer hover:text-gray-700"
            aria-label="Fermer"
            @click="closeEventPopUp()"
          >&times;</button>
        </div>

        <form @submit.prevent="submit">
          <EventTypeSelector v-model="selectedType" :locked="isEditMode" />

          <EventBasicFields v-model:title="title" v-model:description="description" v-model:group="group" />

          <EventSubjectField v-if="isExamen || isDevoir" v-model:subject="subject" :classes="classes" />

          <EventDateLocationFields v-model:location="location" v-model:date="date" v-model:isDevoir="isDevoir" />

          <EventTimeFields v-if="isDevoir" v-model:dueTime="dueTime" />
          
          <EventTimeFields v-else v-model:startTime="startTime" v-model:endTime="endTime" />
          
          <button
            type="submit"
            class="w-full mt-4 py-3 rounded-lg text-white font-medium hover:brightness-90"
            style="background: var(--color-primary)"
          >
            Ajouter au planning
          </button>
        </form>
      </dialog>
    </transition>
  </div>
</template>

<style scoped>
.add-event-card {
  color: var(--color-black);
  background: var(--color-background);
}

.animate-fade-in-up {
  animation: fadeInUp 0.2s ease-out forwards;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(10px) scale(0.98); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

/* Transition classes for Vue <transition name="modal-fade"> */
.modal-fade-enter-from { opacity: 0; transform: translateY(8px) scale(0.99); }
.modal-fade-enter-active { transition: all 180ms ease-out; }
.modal-fade-enter-to { opacity: 1; transform: translateY(0) scale(1); }
.modal-fade-leave-active { transition: all 140ms ease-in; }
.modal-fade-leave-to { opacity: 0; transform: translateY(8px) scale(0.99); }
</style>
