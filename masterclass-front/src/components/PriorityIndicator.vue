<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  priority?: 'urgent' | 'normal' | 'faible' | null;
}>();

const config = computed(() => {
  switch (props.priority) {
    case 'urgent':
      return {
        label: 'Urgent',
        containerClass: 'bg-[var(--color-priority-urgent-bg)] border-[var(--color-priority-urgent-stroke)] text-[var(--color-priority-urgent-txt)]',
        dotClass: 'bg-[var(--color-priority-urgent-txt)]'
      };
    case 'normal':
      return {
        label: 'Normal',
        containerClass: 'bg-[var(--color-priority-normal-bg)] border-[var(--color-priority-normal-stroke)] text-[var(--color-priority-normal-txt)]',
        dotClass: 'bg-[var(--color-priority-normal-txt)]'
      };
    case 'faible':
      return {
        label: 'Faible',
        containerClass: 'bg-[var(--color-priority-faible-bg)] border-[var(--color-priority-faible-stroke)] text-[var(--color-priority-faible-txt)]',
        dotClass: 'bg-[var(--color-priority-faible-txt)]'
      };
    default:
      return null;
  }
});
</script>

<template>
  <div
    v-if="config"
    class="inline-flex items-center gap-1 px-1.5 py-0.5 rounded-sm border text-[9px] font-bold tracking-wider uppercase leading-none"
    :class="config.containerClass"
  >
    <span class="w-1.5 h-1.5 rounded-full" :class="config.dotClass"></span>
    {{ config.label }}
  </div>
</template>
