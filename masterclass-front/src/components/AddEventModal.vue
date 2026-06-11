<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { getClasses } from '@/services/classesService'
import type { Classe } from '@/types/classes'

type EventType = 'devoir' | 'activité' | 'sport'

interface EventPayload {
  type: EventType
  title: string
  description: string
  subject?: string
  location: string
  date: string
  startTime: string
  endTime: string
}

const emit = defineEmits<{
  (e: 'submit', payload: EventPayload): void
}>()

const dialog = ref<boolean>(false)
const classes = ref<Classe[]>([])
const selectedType = ref<EventType>('devoir')

const title = ref<string>('')
const description = ref<string>('')
const subject = ref<string>('')
const location = ref<string>('')
const date = ref<string>(getTodayDate())
const startTime = ref<string>('08:00')
const endTime = ref<string>('09:00')

const typeLabels: Record<EventType, string> = {
  devoir: 'Devoir',
  activité: 'Activité',
  sport: 'Sport',
}

const isDevoir = computed<boolean>(() => selectedType.value === 'devoir')

const dialogTitle = computed<string>(() => {
  const feminineTypes: EventType[] = ['activité']
  const article = feminineTypes.includes(selectedType.value) ? 'Nouvelle' : 'Nouveau'
  const label = typeLabels[selectedType.value] ?? ''
  return `${article} ${label}`
})

const classItems = computed<Classe[]>(() => classes.value)

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
}

function closeEventPopUp(): void {
  dialog.value = false
}

function addEventPopup(): void {
  resetForm()
  dialog.value = true
}

// expose the open method so parent components can trigger the dialog
defineExpose({ addEventPopup })

function submit(): void {
  emit('submit', {
    type: selectedType.value,
    title: title.value,
    description: description.value,
    subject: isDevoir.value ? subject.value : undefined,
    location: location.value,
    date: date.value,
    startTime: startTime.value,
    endTime: endTime.value,
  })

  closeEventPopUp()
}
</script>

<template>

  <v-dialog v-model="dialog" max-width="760">
    <v-card rounded="xl" class="pa-2 pa-md-4">
      <v-card-title class="d-flex align-center justify-space-between text-h4 font-weight-bold py-4">
        <span>{{ dialogTitle }}</span>
        <v-btn icon variant="text" @click="closeEventPopUp()">
          <span aria-hidden="true" style="font-size: 2rem; line-height: 1">×</span>
        </v-btn>
      </v-card-title>

      <v-card-text>
        <v-form @submit.prevent="submit">
          <div class="text-subtitle-1 font-weight-medium mb-2">Type</div>
          <v-btn-toggle
            v-model="selectedType"
            mandatory
            divided
            class="w-100 mb-6 type-toggle"
          >
            <v-btn value="devoir">Devoir</v-btn>
            <v-btn value="activité">Activité</v-btn>
            <v-btn value="sport">Sport</v-btn>
          </v-btn-toggle>

          <v-text-field
            v-model="title"
            label="Titre"
            placeholder="Ex: DiscoFoot"
            variant="outlined"
            class="mb-4"
            required
          />

          <v-textarea
            v-model="description"
            label="Description"
            placeholder="Ex: Une discipline encore méconnue..."
            variant="outlined"
            class="mb-4"
            rows="3"
            auto-grow
          />

          <v-select
            v-if="isDevoir"
            v-model="subject"
            :items="classItems"
            item-title="label"
            item-value="value"
            label="Matière"
            variant="outlined"
            class="mb-4"
          />

          <v-row dense>
            <v-col cols="12" md="6">
              <v-text-field
                v-model="location"
                label="Lieu"
                placeholder="Ex: Chez Clovis"
                variant="outlined"
                class="mb-4"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="date"
                label="Date"
                type="date"
                variant="outlined"
                class="mb-4"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="startTime"
                label="Heure début"
                type="time"
                variant="outlined"
                class="mb-4"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="endTime"
                label="Heure fin"
                type="time"
                variant="outlined"
                class="mb-4"
              />
            </v-col>
          </v-row>

          <v-btn
            type="submit"
            color="teal-darken-1"
            size="large"
            block
            rounded="lg"
            class="mt-2"
          >
            Ajouter au planning
          </v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<style scoped>
.type-toggle :deep(.v-btn) {
  min-height: 56px;
  text-transform: none;
}
</style>
