<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  type?: 'devoir' | 'activite' | 'sport';
}>();

const badgeConfig = computed(() => {
  switch (props.type) {
    case 'devoir':
      return { label: 'DEVOIR', class: 'bg-[var(--color-tag-devoir-bg)] text-[var(--color-tag-devoir-txt)] border-[var(--color-tag-devoir-stroke)]' };
    case 'activite':
      return { label: 'ACTIVITÉ', class: 'bg-[var(--color-tag-activite-bg)] text-[var(--color-tag-activite-txt)] border-[var(--color-tag-activite-stroke)]' };
    case 'sport':
      return { label: 'SPORT', class: 'bg-[var(--color-tag-sport-bg)] text-[var(--color-tag-sport-txt)] border-[var(--color-tag-sport-stroke)]' };
    default:
      return null;
  }
});
</script>

<template>
  <span
    v-if="badgeConfig"
    class="px-2 py-0.5 rounded text-[10px] font-bold tracking-wide border shrink-0 uppercase"
    :class="badgeConfig.class"
  >
    {{ badgeConfig.label }}
  </span>
</template>
