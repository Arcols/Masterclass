<script setup lang="ts">
import type { EventType } from '@/types/events.ts'
import type { EventTypeOption } from '@/types/event.ts'

const typeOptions: EventTypeOption[] = [
  { id: 'devoir', label: 'Devoir' },
  { id: 'examen', label: 'Examen' },
  { id: 'activite', label: 'Activité' },
  { id: 'sport', label: 'Sport' },
]

withDefaults(
  defineProps<{
    modelValue: EventType
    locked?: boolean
  }>(),
  {
    locked: false,
  }
)

defineEmits<{
  'update:modelValue': [value: EventType]
}>()
</script>
<template>
  <div>
    <div class="text-sm font-medium text-gray-700 mb-2">Type</div>
    <div class="w-full mb-6 bg-gray-100 rounded-lg p-1 flex gap-2">
      <button
        v-for="option in typeOptions"
        :key="option.id"
        type="button"
        class="flex-1 h-10 rounded-lg text-sm font-medium transition-shadow px-4"
        :class="[
          modelValue === option.id ? 'bg-white !text-[var(--color-primary)]' : 'text-gray-600',
          locked && modelValue !== option.id ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer',
        ]"
        :disabled="locked && modelValue !== option.id"
        @click="!locked && $emit('update:modelValue', option.id)"
      >
        {{ option.label }}
      </button>
    </div>
  </div>
</template>

<style scoped>
</style>
