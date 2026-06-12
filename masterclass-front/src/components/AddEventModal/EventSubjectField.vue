<script setup lang="ts">
import type { Classe } from '@/types/classes'

defineProps<{
  subject: string
  classes: Classe[]
}>()

defineEmits<{
  'update:subject': [value: string]
}>()

function isFieldValid(value: unknown): boolean {
  if (value === null || value === undefined) return false
  if (Array.isArray(value) && value.length === 0) return false
  return String(value).trim().length > 0
}

const requiredRule = (value: unknown): true | string => {
  return isFieldValid(value) || 'Champ obligatoire'
}
</script>

<template>
  <v-select
    :model-value="subject"
    @update:model-value="$emit('update:subject', $event)"
    :items="classes"
    item-title="label"
    item-value="value"
    label="Matière *"
    variant="outlined"
    class="mb-4"
    required
    :rules="[requiredRule]"
  />
</template>
