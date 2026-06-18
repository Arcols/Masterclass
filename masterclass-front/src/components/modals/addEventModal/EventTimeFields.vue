<script setup lang="ts">
import { useTimeOptions } from '@/composables/useTimeOptions'

const props = defineProps<{
  startTime?: string
  endTime?: string
}>()

const timeOptions = useTimeOptions()
</script>
<template>
  <div class="mb-4">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div class="relative">
        <label class="block text-sm font-medium text-gray-700 mb-1">Heure début <span class="text-[var(--color-red)]">*</span></label>
        <select
          :value="startTime"
          @change="$emit('update:startTime', ($event.target as HTMLSelectElement).value)"
          class="w-full mb-4 px-4 py-3 bg-white border border-gray-200 rounded-lg appearance-none focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-sm"
          required
        >
          <option v-for="t in timeOptions" :key="t" :value="t">{{ t }}</option>
        </select>
        <span class="pointer-events-none absolute right-3 top-[42px] text-gray-500">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
        </span>
      </div>

      <div class="relative">
        <label class="block text-sm font-medium text-gray-700 mb-1">Heure fin <span class="text-[var(--color-red)]">*</span></label>
        <select
          :value="endTime"
          @change="$emit('update:endTime', ($event.target as HTMLSelectElement).value)"
          class="w-full mb-4 px-4 py-3 bg-white border border-gray-200 rounded-lg appearance-none focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-sm"
          required
        >
          <option v-for="t in timeOptions" :key="t" :value="t">{{ t }}</option>
        </select>
        <span class="pointer-events-none absolute right-3 top-[42px] text-gray-500">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
        </span>
      </div>
    </div>
  </div>

</template>