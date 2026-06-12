<script setup lang="ts">
import type { Classe } from '@/types/classes.ts'

defineProps<{
  subject: string
  classes: Classe[]
}>()

defineEmits<{
  'update:subject': [value: string]
}>()

</script>

<template>
  <label class="block text-sm font-medium text-gray-700 mb-1">Matière *</label>
  <div class="relative">
    <select
      :value="subject"
      @change="$emit('update:subject', ($event.target as HTMLSelectElement).value)"
      class="w-full mb-4 px-4 py-3 bg-white border border-gray-200 rounded-md placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-sm appearance-none pr-10"
      required
    >
      <option v-for="c in classes" :key="c.value" :value="c.value">{{ c.label }}</option>
    </select>

    <svg
      class="pointer-events-none absolute inset-y-0 right-3 my-auto h-4 w-4 text-gray-500"
      fill="none"
      stroke="currentColor"
      viewBox="0 0 24 24"
      aria-hidden="true"
    >
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
    </svg>
  </div>
</template>
