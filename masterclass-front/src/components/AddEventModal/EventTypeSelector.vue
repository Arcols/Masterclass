<script setup lang="ts">
import type { EventType } from '@/types/events'

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

const typeLabels: Record<EventType, string> = {
  devoir: 'Devoir',
  activite: 'Activité',
  sport: 'Sport',
}
</script>
<template>
  <div>
    <div class="text-sm font-medium text-gray-700 mb-2">Type</div>
    <div class="w-full mb-6 bg-gray-100 rounded-lg p-1 flex gap-2">
      <button
        type="button"
        class="flex-1 h-10 rounded-lg text-sm font-medium transition-shadow px-4"
        :class="[
          modelValue === 'devoir' ? 'bg-white !text-[var(--color-primary)]' : 'text-gray-600',
          locked && modelValue !== 'devoir' ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer',
        ]"
        :disabled="locked && modelValue !== 'devoir'"
        @click="!locked && $emit('update:modelValue', 'devoir')"
      >
        {{ typeLabels.devoir }}
      </button>

      <button
        type="button"
        class="flex-1 h-10 rounded-lg text-sm font-medium transition-shadow px-4"
        :class="[
          modelValue === 'activite' ? 'bg-white !text-[var(--color-primary)]' : 'text-gray-600',
          locked && modelValue !== 'activite' ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer',
        ]"
        :disabled="locked && modelValue !== 'activite'"
        @click="!locked && $emit('update:modelValue', 'activite')"
      >
        {{ typeLabels.activite }}
      </button>

      <button
        type="button"
        class="flex-1 h-10 rounded-lg text-sm font-medium transition-shadow px-4"
        :class="[
          modelValue === 'sport' ? 'bg-white !text-[var(--color-primary)]' : 'text-gray-600',
          locked && modelValue !== 'sport' ? 'opacity-40 cursor-not-allowed' : 'cursor-pointer',
        ]"
        :disabled="locked && modelValue !== 'sport'"
        @click="!locked && $emit('update:modelValue', 'sport')"
      >
        {{ typeLabels.sport }}
      </button>
    </div>
  </div>
</template>

<style scoped>
/* styles kept minimal — visual handled by Tailwind classes in template */
</style>
