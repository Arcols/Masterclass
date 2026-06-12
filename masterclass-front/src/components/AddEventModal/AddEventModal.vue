<script setup lang="ts">
import { computed, onMounted, onBeforeUnmount, ref } from 'vue'
import { getClasses } from '@/services/classesService'
import { createEvent,updateEvent } from '@/services/eventService'
import type { Classe } from '@/types/classes'
import type { EventType, EventPayload } from '@/types/events'
import EventTypeSelector from './EventTypeSelector.vue'
import EventBasicFields from './EventBasicFields.vue'
import EventSubjectField from './EventSubjectField.vue'
import EventDateTimeFields from './EventDateTimeFields.vue'

const props = withDefaults(
  defineProps<{
    eventToEdit?: EventPayload
  }>(),
  {
    eventToEdit: undefined,
  }
)

const dialog = ref<boolean>(false)
const classes = ref<Classe[]>([])
var selectedType:EventType;

if(props.eventToEdit){
  selectedType = ref<EventType>(props.eventToEdit.type)
} else {
  selectedType = ref<EventType>('devoir')
}

const isEditMode = ref<boolean>(false)
const eventIdToEdit = ref<number | string | undefined>(undefined)

const title = ref<string>('')
const description = ref<string>('')
const subject = ref<string>('')
const location = ref<string>('')
const date = ref<string>(getTodayDate())
const startTime = ref<string>('08:00')
const endTime = ref<string>('09:00')

const typeLabels: Record<EventType, string> = {
  devoir: 'Devoir',
  activite: 'Activité',
  sport: 'Sport',
}

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
  subject.value = classes.value[0]?.value ?? ''
  location.value = ''
  date.value = getTodayDate()
  startTime.value = '08:00'
  endTime.value = '09:00'
  isEditMode.value = false
  eventIdToEdit.value = undefined
}

function closeEventPopUp(): void {
  dialog.value = false
}

function closeOnEsc(event: KeyboardEvent): void {
  if (event.key === 'Escape' || event.key === 'Esc') {
    if (dialog.value) {
      closeEventPopUp()
    }
  }
}

function updateEvent(eventToEdit: EventPayload) {
  isEditMode.value = true
  eventIdToEdit.value = eventToEdit.id
  selectedType.value = eventToEdit.type
  title.value = eventToEdit.title
  description.value = eventToEdit.description
  subject.value = eventToEdit.subject ?? ''
  location.value = eventToEdit.location
  date.value = eventToEdit.date
  startTime.value = eventToEdit.startTime
  endTime.value = eventToEdit.endTime
}

function addEventPopup(eventToEdit?: EventPayload): void {
  resetForm()

  if (eventToEdit) {
    updateEvent(eventToEdit);
  }

  dialog.value = true
}

defineExpose({ addEventPopup })

async function submit(): Promise<void> {
  if (!isFieldValid(title.value) || !isFieldValid(location.value) || !isFieldValid(date.value) || !isFieldValid(startTime.value) || !isFieldValid(endTime.value)) {
    return
  }

  const payload: EventPayload = {
    id: eventIdToEdit.value,
    type: selectedType.value,
    title: title.value,
    description: description.value,
    subject: isDevoir.value ? subject.value : undefined,
    location: location.value,
    date: date.value,
    startTime: startTime.value,
    endTime: endTime.value,
  }

  try {
    if (isEditMode.value) {
      await updateEvent(eventIdToEdit.value as number | string, {
        payload,
      })
    } else {
      await createEvent({
        payload,
      })
    }
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
  <div v-if="dialog" class="fixed inset-0 z-50 flex items-center justify-center">
    <div class="absolute inset-0 bg-black/40" @click="closeEventPopUp()"></div>

    <div :class="['add-event-card', selectedType]" class="relative bg-white rounded-xl w-full max-w-3xl mx-4 p-6 shadow-xl z-10">
      <div class="flex items-center justify-between mb-4">
        <h3 class="text-2xl font-bold">{{ dialogTitle }}</h3>
        <button type="button" class="text-3xl leading-none text-gray-500" @click="closeEventPopUp()">&times;</button>
      </div>

      <form @submit.prevent="submit">
        <EventTypeSelector v-model="selectedType" />

        <EventBasicFields v-model:title="title" v-model:description="description" />

        <EventSubjectField v-if="isDevoir" v-model:subject="subject" :classes="classes" />

        <EventDateTimeFields v-model:location="location" v-model:date="date" v-model:startTime="startTime" v-model:endTime="endTime" />

        <button
          type="submit"
          class="w-full mt-4 py-3 rounded-lg text-white font-medium hover:brightness-90"
          style="background: var(--color-primary)"
        >
          Ajouter au planning
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.add-event-card {
  color: var(--color-black);
  background: var(--color-background);
}

/* keep a small compatibility hook for selected type classes */
.add-event-card.devoir { }
.add-event-card.activite { }
.add-event-card.sport { }
</style>
