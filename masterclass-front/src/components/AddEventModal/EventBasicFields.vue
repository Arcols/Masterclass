<script setup lang="ts">
defineProps<{
  title: string
  description: string
}>()

defineEmits<{
  'update:title': [value: string]
  'update:description': [value: string]
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
  <div>
    <v-text-field
      :model-value="title"
      @update:model-value="$emit('update:title', $event)"
      label="Titre *"
      placeholder="Ex: DiscoFoot"
      variant="outlined"
      class="mb-4"
      required
      :rules="[requiredRule]"
    />

    <v-textarea
      :model-value="description"
      @update:model-value="$emit('update:description', $event)"
      label="Description"
      placeholder="Ex: Une discipline encore méconnue..."
      variant="outlined"
      class="mb-4"
      rows="3"
      auto-grow
    />
  </div>
</template>
