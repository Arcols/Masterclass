<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
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
  <v-dialog v-model="dialog" max-width="760">
    <v-card :class="['add-event-card', selectedType]" rounded="xl" class="pa-2 pa-md-4">
      <v-card-title class="d-flex align-center justify-space-between text-h4 font-weight-bold py-4">
        <span>{{ dialogTitle }}</span>
        <v-btn icon variant="text" @click="closeEventPopUp()">
          <span aria-hidden="true" style="font-size: 2rem; line-height: 1">×</span>
        </v-btn>
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="submit">
          <EventTypeSelector
            v-model="selectedType"
          />

          <EventBasicFields
            v-model:title="title"
            v-model:description="description"
          />

          <EventSubjectField
            v-if="isDevoir"
            v-model:subject="subject"
            :classes="classes"
          />

          <EventDateTimeFields
            v-model:location="location"
            v-model:date="date"
            v-model:startTime="startTime"
            v-model:endTime="endTime"
          />

          <v-btn
            type="submit"
            size="large"
            block
            rounded="lg"
            class="mt-2 submit-btn"
          >
            Ajouter au planning
          </v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<style scoped>
.add-event-card {
  color: var(--color-black);
  background: var(--color-background);
}

.add-event-card :deep(.v-card-title) {
  color: var(--color-black);
}

.submit-btn :deep(.v-btn__content), .submit-btn {
  background: var(--color-primary) !important;
  color: white !important;
}
</style>
