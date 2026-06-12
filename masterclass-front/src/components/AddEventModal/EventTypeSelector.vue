<script setup lang="ts">
import type { EventType } from '@/types/events'

defineProps<{
  modelValue: EventType
}>()

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
    <div class="text-subtitle-1 font-weight-medium mb-2">Type</div>
    <v-btn-toggle
      :model-value="modelValue"
      @update:model-value="$emit('update:modelValue', $event)"
      mandatory
      divided
      class="w-100 mb-6 type-toggle"
    >
      <v-btn value="devoir" :class="{ selected: modelValue === 'devoir' }">{{ typeLabels.devoir }}</v-btn>
      <v-btn value="activite" :class="{ selected: modelValue === 'activite' }">{{ typeLabels.activite }}</v-btn>
      <v-btn value="sport" :class="{ selected: modelValue === 'sport' }">{{ typeLabels.sport }}</v-btn>
    </v-btn-toggle>
  </div>
</template>

<style scoped>
.type-toggle {
  background: #f1f2f4;
  border-radius: 12px;
  padding: 6px;
  display: flex;
  align-items: center;
}
.type-toggle :deep(.v-btn) {
  flex: 1 1 0;
  height: 56px;
  min-height: 56px;
  line-height: 1;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  text-transform: none;
  background: transparent !important;
  color: #6b7280;
  box-shadow: none !important;
  border-radius: 8px;
  padding: 0 18px !important;
}
.type-toggle :deep(.v-btn[aria-pressed="true"]),
.type-toggle :deep(.v-btn.v-btn--active),
.type-toggle :deep(.v-btn.v-btn--selected),
.type-toggle :deep(.v-btn.selected),
.type-toggle :deep(.v-btn[aria-pressed="true"] .v-btn__content),
.type-toggle :deep(.v-btn.v-btn--active .v-btn__content),
.type-toggle :deep(.v-btn.v-btn--selected .v-btn__content),
.type-toggle :deep(.v-btn.selected .v-btn__content) {
  background: white !important;
  color: #16a34a !important; /* fallback green */
  box-shadow: 0 6px 14px rgba(16, 24, 40, 0.06) !important;
  margin: 0 6px;
  border-radius: 8px !important;
}

/* If theme variable exists prefer it (kept for compatibility) */
.type-toggle :deep(.v-btn[aria-pressed="true"]),
.type-toggle :deep(.v-btn.v-btn--active),
.type-toggle :deep(.v-btn.v-btn--selected) {
  color: var(--color-primary) !important;
}
</style>
