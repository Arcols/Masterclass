<script setup lang="ts">
import type { EventType } from '@/types/events.ts'

type TypeOption = {
  value: EventType
  label: string
}

const typeOptions: TypeOption[] = [
  { value: 'devoir', label: 'Devoir' },
  { value: 'examen', label: 'Examen' },
  { value: 'activite', label: 'Activité' },
  { value: 'sport', label: 'Sport' },
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
        :key="option.value"
        type="button"
        class="flex-1 h-10 rounded-lg text-sm font-medium transition-shadow px-4"
        :class="[
          modelValue === option.value ? 'bg-white !text-[var(--color-primary)]' : 'text-gray-600',
          locked && modelValue !== option.value ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer',
        ]"
        :disabled="locked && modelValue !== option.value"
        @click="!locked && $emit('update:modelValue', option.value)"
      >
        {{ option.label }}
      </button>
    </div>
  </div>
</template>

<style scoped>
</style>
