<script setup lang="ts">
defineProps<{
  author?: string;
  datetime: string;
  content: string;
  isNote?: boolean;
}>();
</script>

<template>
  <div class="flex flex-col mb-4">
    <div
      class="flex items-center mb-1 text-[11px] md:text-xs font-semibold"
      :class="isNote ? 'justify-end' : 'justify-between'"
    >
      <span v-if="!isNote" class="text-[var(--color-primary)]">
        Par {{ author }}
      </span>
      <span class="text-[var(--color-primary)]">{{ datetime }}</span>
    </div>

    <div class="bg-white border border-gray-200 rounded-lg p-2.5 md:p-3 text-xs md:text-sm text-gray-700 shadow-sm whitespace-pre-wrap">
      {{ content }}
    </div>
  </div>
</template>
