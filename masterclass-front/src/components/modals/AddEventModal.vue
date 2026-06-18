<script setup lang="ts">
import { computed, onMounted, onBeforeUnmount, ref, watch } from 'vue'
import { getClasses } from '@/services/classesService.ts'
import { createEvent,updateEvent } from '@/services/eventService.ts'
import type { Classe } from '@/types/classes.ts'
import type { EventType, EventPayload } from '@/types/events.ts'
import EventTypeSelector from '@/components/modals/addEventModal/EventTypeSelector.vue'
import EventBasicFields from '@/components/modals/addEventModal/EventBasicFields.vue'
import EventSubjectField from '@/components/modals/addEventModal/EventSubjectField.vue'
import EventDateLocationFields from '@/components/modals/addEventModal/EventDateLocationFields.vue'
import EventTimeFields from './addEventModal/EventTimeFields.vue'
import EventDevoirDateLink from './addEventModal/EventDevoirDateLink.vue'

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
const groupError = ref<boolean>(false)
const subject = ref<string>('')
const location = ref<string>('')
const date = ref<string>(getTodayDate())
const startTime = ref<string>('08:00')
const endTime = ref<string>('09:00')
const submissionLink = ref<string>('')

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
  groupError.value = false
  subject.value = classes.value[0]?.value ?? ''
  location.value = ''
  date.value = getTodayDate()
  startTime.value = '08:00'
  endTime.value = '09:00'
  isEditMode.value = false
  eventIdToEdit.value = undefined
  submissionLink.value = ''
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
  groupError.value = false
  subject.value = eventToEdit.subject ?? ''
  location.value = eventToEdit.location
  date.value = eventToEdit.date
  startTime.value = eventToEdit.startTime
  endTime.value = eventToEdit.endTime
  submissionLink.value = eventToEdit.submissionLink ?? ''
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
      submissionLink.value = eventToEdit.submissionLink ?? submissionLink.value
    }
  }

  isDialogVisible.value = true
  dialogRef.value?.showModal()
}

watch(group, (val) => {
  if (isFieldValid(val)) {
    groupError.value = false
  }
})

defineExpose({ addEventPopup })

function getOneHourBefore(time: string): string {
  const hours = Number(time.slice(0, 2))
  const minutes = Number(time.slice(3, 5))
  const totalMinutes = (hours * 60 + minutes - 60 + 24 * 60) % (24 * 60)
  const computedHours = String(Math.floor(totalMinutes / 60)).padStart(2, '0')
  const computedMinutes = String(totalMinutes % 60).padStart(2, '0')
  return `${computedHours}:${computedMinutes}`
}

async function submit(): Promise<void> {
  const effectiveStartTime = isDevoir.value ? getOneHourBefore(endTime.value) : startTime.value
  const start = Number(effectiveStartTime.slice(0, 2)) * 60 + Number(effectiveStartTime.slice(3, 5))
  const end = Number(endTime.value.slice(0, 2)) * 60 + Number(endTime.value.slice(3, 5))
  if (
    !isFieldValid(title.value) ||
    (!isDevoir.value && !isFieldValid(location.value)) ||
    !isFieldValid(date.value) ||
    (!isDevoir.value && !isFieldValid(startTime.value)) ||
    !isFieldValid(endTime.value) ||
    !isFieldValid(group.value) ||
    (isExamen.value && !isFieldValid(subject.value)) ||
    (isDevoir.value && !isFieldValid(subject.value)) ||
    (isDevoir.value && !isFieldValid(endTime.value)) ||
    (!isDevoir.value && end <= start)
  ) {
    if (!isFieldValid(group.value)) {
      groupError.value = true
    }
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
    startTime: effectiveStartTime,
    endTime: endTime.value,
    submissionLink: isDevoir.value ? submissionLink.value : undefined,
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
        class="relative bg-white rounded-xl w-full max-w-3xl mx-0 my-6 shadow-xl z-10 border-none animate-fade-in-up max-h-[calc(100dvh-7rem)] overflow-hidden flex flex-col"
        @keydown.esc.prevent="closeEventPopUp()"
      >
        <div class="shrink-0 flex items-center justify-between px-4 sm:px-6 py-4 border-b border-gray-100 bg-white">
          <h3 class="text-2xl font-bold">{{ dialogTitle }}</h3>
          <button
            type="button"
            class="text-3xl leading-none text-gray-500 cursor-pointer hover:text-gray-700"
            aria-label="Fermer"
            @click="closeEventPopUp()"
          >&times;</button>
        </div>

        <form id="add-event-form" class="min-h-0 flex-1 overflow-y-auto overscroll-contain px-4 sm:px-6 py-4" @submit.prevent="submit">
          <EventTypeSelector v-model="selectedType" :locked="isEditMode" />

          <EventBasicFields v-model:title="title" v-model:description="description" v-model:group="group" :show-group-error="groupError" />

          <EventSubjectField v-if="isExamen || isDevoir" v-model:subject="subject" :classes="classes" />

          <EventDateLocationFields v-if="!isDevoir" v-model:location="location" v-model:date="date" v-model:isDevoir="isDevoir" />
          
          <EventTimeFields v-if="!isDevoir" v-model:startTime="startTime" v-model:endTime="endTime" />

          <EventDevoirDateLink v-if="isDevoir" v-model:submissionLink="submissionLink" v-model:date="date" v-model:endTime="endTime" v-model:isDevoir="isDevoir" />
        </form>

        <div class="shrink-0 flex items-center gap-3 px-4 sm:px-6 py-4 border-t border-gray-100 bg-white">
          <button
            type="submit"
            form="add-event-form"
            class="w-full py-3 rounded-lg text-white font-medium hover:brightness-90 cursor-pointer"
            style="background: var(--color-primary)"
          >
            Ajouter au planning
          </button>
        </div>
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
