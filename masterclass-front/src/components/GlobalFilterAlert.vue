<script setup lang="ts">
import { computed } from 'vue';
import { useFilters } from '@/composables/useFilters';

const { selectedTypes, selectedGroups, showFavoritesOnly } = useFilters();

const hasActiveFilters = computed(() => {
  return selectedTypes.value.length > 0 || selectedGroups.value.length > 0 || showFavoritesOnly.value;
});

const filterSummary = computed(() => {
  const parts = [];
  if (selectedTypes.value.length > 0) parts.push(`${selectedTypes.value.length} type(s)`);
  if (selectedGroups.value.length > 0) parts.push(`${selectedGroups.value.length} groupe(s)`);
  if (showFavoritesOnly.value) parts.push('Favoris');

  return parts.join(', ');
});
</script>

<template>
  <div
    v-if="hasActiveFilters"
    class="w-full bg-[var(--color-primary)]/10 border-b border-[var(--color-primary)]/20 px-3 py-1.5 md:px-4 md:py-2 flex items-center justify-center shrink-0 z-40 transition-all"
  >
    <span class="text-xs md:text-sm font-medium text-[var(--color-primary)] text-center">
      Filtres globaux actifs : <span class="font-semibold">{{ filterSummary }}</span>
    </span>
  </div>
</template>
