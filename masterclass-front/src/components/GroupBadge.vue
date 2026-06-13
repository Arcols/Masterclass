<script setup lang="ts">
import { XMarkIcon } from '@heroicons/vue/24/outline';

defineProps<{
  group: string;
  removable?: boolean;
}>();

const emit = defineEmits<{
  (e: 'remove'): void;
}>();
</script>

<template>
  <span
    class="inline-flex items-center gap-1 px-2 py-0.5 rounded text-[10px] font-bold tracking-wide border shrink-0 uppercase bg-[var(--color-groupe-bg)] border-[var(--color-groupe-border)] text-[var(--color-groupe-txt)]"
  >
    {{ group }}
    <button
      v-if="removable"
      @click.stop="emit('remove')"
      class="hover:scale-125 hover:text-[var(--color-red)] focus:outline-none flex items-center justify-center cursor-pointer"
    >
      <XMarkIcon class="w-3 h-3" stroke-width="3" />
    </button>
  </span>
</template>
