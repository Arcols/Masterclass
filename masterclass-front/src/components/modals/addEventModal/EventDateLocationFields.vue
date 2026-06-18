<script setup lang="ts">
import { ref } from 'vue'
import { fr } from 'date-fns/locale/fr'
import { useDatePicker } from '@/composables/useDatePicker'

const props = defineProps<{
  location: string
  date: string
  isDevoir: boolean
}>()

const emit = defineEmits<{
  'update:location': [value: string]
  'update:date': [value: string]
  'update:startTime': [value: string]
  'update:endTime': [value: string]
}>()

const {
  datePickerOpen,
  datePickerRef,
  calendarMonth,
  selectedDate,
  calendarDays,
  weekdayLabels,
  toggleDatePicker,
  selectDay,
  previousMonth,
  nextMonth,
  format,
  isSameDay,
  isSameMonth,
  isToday,
} = useDatePicker(props.date)

function handleSelect(day: Date) {
  emit('update:date', selectDay(day))
}
</script>

<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">Lieu <span class="text-[var(--color-red)]">*</span></label>
      <input
        type="text"
        :value="location"
        @input="$emit('update:location', ($event.target as HTMLInputElement).value)"
        placeholder="Ex: Chez Clovis"
        class="w-full mb-4 px-4 py-3 bg-white border border-gray-200 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-sm"
        required
      />
    </div>

    <div class="relative" ref="datePickerRef">
      <label class="block text-sm font-medium text-gray-700 mb-1">Date <span class="text-[var(--color-red)]">*</span></label>
      <button
        type="button"
        class="w-full mb-4 px-4 py-3 bg-white border border-gray-200 rounded-lg text-left flex items-center justify-between gap-3 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-sm"
        @click="toggleDatePicker()"
      >
        <span class="text-gray-900">
          {{ date ? format(selectedDate, 'dd/MM/yyyy', { locale: fr }) : 'Choisir une date' }}
        </span>
        <svg class="w-5 h-5 text-gray-500 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2Z"
          />
        </svg>
      </button>

      <div v-if="datePickerOpen" class="absolute left-0 top-[calc(100%+4px)] z-30 w-full rounded-xl border border-gray-200 bg-white p-3 shadow-lg">
        <div class="flex items-center justify-between mb-3">
          <button type="button" class="rounded-md px-2 py-1 text-gray-600 hover:bg-gray-100" @click="previousMonth()">‹</button>
          <div class="font-medium text-gray-900 capitalize">
            {{ format(calendarMonth, 'MMMM yyyy', { locale: fr }) }}
          </div>
          <button type="button" class="rounded-md px-2 py-1 text-gray-600 hover:bg-gray-100" @click="nextMonth()">›</button>
        </div>

        <div class="grid grid-cols-7 gap-1 text-center text-xs font-medium text-gray-500 mb-2">
          <div v-for="label in weekdayLabels" :key="label" class="py-1">{{ label }}</div>
        </div>

        <div class="grid grid-cols-7 gap-1">
            <button
              v-for="day in calendarDays"
              :key="day.toISOString()"
              type="button"
              class="h-9 rounded-md text-sm transition-colors"
              :class="[
                isSameMonth(day, calendarMonth)
                  ? 'text-gray-900 hover:bg-gray-100'
                  : 'text-gray-300',
                isSameDay(day, selectedDate)
                  ? 'bg-[var(--color-primary)] text-white hover:bg-[var(--color-primary)]'
                  : '',
                isToday(day) && !isSameDay(day, selectedDate) ? 'border border-[var(--color-primary)]' : '',
              ]"
              @click="handleSelect(day)"
            >
              {{ format(day, 'd') }}
            </button>
        </div>
      </div>
    </div>
  </div>
</template>
