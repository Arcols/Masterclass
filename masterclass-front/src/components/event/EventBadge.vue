<script setup lang="ts">
import { computed } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline';
import type { EventType } from '@/types/event.ts'

const props = defineProps<{
  type?: EventType;
  removable?: boolean; // Permet d'afficher la croix
}>();

const emit = defineEmits<{
  (e: 'remove'): void;
}>();

const badgeConfig = computed(() => {
  switch (props.type) {
    case 'devoir':
      return { label: 'DEVOIR', class: 'bg-[var(--color-tag-devoir-bg)] text-[var(--color-tag-devoir-txt)] border-[var(--color-tag-devoir-border)]' };
    case 'activite':
      return { label: 'ACTIVITÉ', class: 'bg-[var(--color-tag-activite-bg)] text-[var(--color-tag-activite-txt)] border-[var(--color-tag-activite-border)]' };
    case 'sport':
      return { label: 'SPORT', class: 'bg-[var(--color-tag-sport-bg)] text-[var(--color-tag-sport-txt)] border-[var(--color-tag-sport-border)]' };
    default:
      return null;
  }
});
</script>

<template>
  <span
    v-if="badgeConfig"
    class="hidden md:inline-flex items-center gap-1 px-1 py-0.3 rounded text-[8px] md:text-[10px] font-bold tracking-wide border shrink-0 uppercase"
    :class="badgeConfig.class"
  >
    {{ badgeConfig.label }}

    <button
      v-if="removable"
      @click.stop="emit('remove')"
      class="hover:scale-125 hover:text-[var(--color-red)] focus:outline-none flex items-center justify-center cursor-pointer"
    >
      <XMarkIcon class="w-3 h-3" stroke-width="3" />
    </button>
  </span>
</template>
