<script setup lang="ts">
defineProps<{
  title: string
  description: string
}>()

defineEmits<{
  'update:title': [value: string]
  'update:description': [value: string]
}>()

</script>

<template>
  <div>
    <label class="block text-sm font-medium text-gray-700 mb-1">Titre *</label>
    <input
      type="text"
      :value="title"
      @input="$emit('update:title', ($event.target as HTMLInputElement).value)"
      placeholder="Ex: DiscoFoot"
      class="w-full mb-4 px-4 py-3 bg-white border border-gray-200 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-sm"
      required
    />

    <label class="block text-sm font-medium text-gray-700 mb-1">Description</label>
    <textarea
      :value="description"
      @input="$emit('update:description', ($event.target as HTMLTextAreaElement).value)"
      placeholder="Ex: Une discipline encore méconnue..."
      class="w-full mb-4 px-4 py-3 bg-white border border-gray-200 rounded-lg placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-[var(--color-primary)] shadow-sm"
      rows="3"
    />
  </div>
</template>
