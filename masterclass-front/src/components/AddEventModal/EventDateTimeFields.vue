<script setup lang="ts">
defineProps<{
  location: string
  date: string
  startTime: string
  endTime: string
}>()

defineEmits<{
  'update:location': [value: string]
  'update:date': [value: string]
  'update:startTime': [value: string]
  'update:endTime': [value: string]
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
  <v-row dense>
    <v-col cols="12" md="6">
      <v-text-field
        :model-value="location"
        @update:model-value="$emit('update:location', $event)"
        label="Lieu *"
        placeholder="Ex: Chez Clovis"
        variant="outlined"
        class="mb-4"
        required
        :rules="[requiredRule]"
      />
    </v-col>

    <v-col cols="12" md="6">
      <v-text-field
        :model-value="date"
        @update:model-value="$emit('update:date', $event)"
        label="Date *"
        type="date"
        variant="outlined"
        class="mb-4"
        required
        :rules="[requiredRule]"
      />
    </v-col>

    <v-col cols="12" md="6">
      <v-text-field
        :model-value="startTime"
        @update:model-value="$emit('update:startTime', $event)"
        label="Heure début *"
        type="time"
        variant="outlined"
        class="mb-4"
        required
        :rules="[requiredRule]"
      />
    </v-col>

    <v-col cols="12" md="6">
      <v-text-field
        :model-value="endTime"
        @update:model-value="$emit('update:endTime', $event)"
        label="Heure fin *"
        type="time"
        variant="outlined"
        class="mb-4"
        required
        :rules="[requiredRule]"
      />
    </v-col>
  </v-row>
</template>
