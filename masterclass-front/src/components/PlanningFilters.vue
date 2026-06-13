<script setup lang="ts">
import MultiSelectDropdown from './MultiSelectDropdown.vue';
import GroupBadge from './GroupBadge.vue';
import EventBadge from '@/components/event/EventBadge.vue'
import type { EventType, EventTypeOption } from '@/types/event.ts'

defineProps<{
  selectedTypes: EventType[];
  selectedGroups: string[];
  availableGroups: string[];
}>();

const emit = defineEmits(['update:selectedTypes', 'update:selectedGroups']);

const typeOptions: EventTypeOption[] = [
  { id: 'devoir', label: 'Devoir' },
  { id: 'activite', label: 'Activité' },
  { id: 'sport', label: 'Sport' }
];
</script>

<template>
  <div class="flex flex-wrap items-center gap-6 px-4 py-3 bg-white border-b border-gray-200 shrink-0">

    <div class="flex items-center gap-2">
      <span class="text-sm font-medium text-[var(--color-black)]">Type :</span>
      <MultiSelectDropdown
        :options="typeOptions"
        :modelValue="selectedTypes"
        @update:modelValue="emit('update:selectedTypes', $event)"
      >
        <template #selected="{ remove }">
          <EventBadge
            v-for="t in selectedTypes"
            :key="t"
            :type="t"
            removable
            @remove="remove(t)"
          />
        </template>
        <template #option="{ option }">
          <EventBadge :type="option.id as EventType" />
        </template>
      </MultiSelectDropdown>
    </div>

    <div class="flex items-center gap-2">
      <span class="text-sm font-medium text-[var(--color-black)]">Groupe :</span>
      <MultiSelectDropdown
        :options="availableGroups.map(g => ({ id: g, label: g }))"
        :modelValue="selectedGroups"
        @update:modelValue="emit('update:selectedGroups', $event)"
      >
        <template #selected="{ remove }">
          <GroupBadge
            v-for="g in selectedGroups"
            :key="g"
            :group="g"
            removable
            @remove="remove(g)"
          />
        </template>
        <template #option="{ option }">
          <GroupBadge :group="option.id" />
        </template>
      </MultiSelectDropdown>
    </div>

  </div>
</template>
