<script setup lang="ts">
import { computed } from 'vue'
import MultiSelectDropdown from '@/components/MultiSelectDropdown.vue'
import { useGroups } from '@/composables/useGroups'
import GroupBadge from '@/components/GroupBadge.vue'

const props = defineProps<{
  title: string
  description: string
  group: string
  showGroupError?: boolean
}>()

const emit = defineEmits<{
  'update:title': [value: string]
  'update:description': [value: string]
  'update:group': [value: string]
}>()

const groupOptions = useGroups()
const groupDropdownOptions = groupOptions.map((group) => ({ id: group, label: group }))

const selectedGroupValues = computed<string[]>({
  get: () => (props.group ? [props.group] : []),
  set: (values) => {
    emit('update:group', values.at(-1) ?? '')
  },
})

</script>

<template>
  <div>
    <label class="block text-sm font-medium text-gray-700 mb-1">Titre <span class="text-[var(--color-red)]">*</span></label>
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
    <div class="flex gap-2">
      <label class="block text-sm font-medium text-gray-700 mb-1">Groupe <span class="text-[var(--color-red)]">*</span></label>
      <div :class="[props.showGroupError ? 'ring-2 ring-[var(--color-red)]/30 rounded-md p-1' : '']">
        <MultiSelectDropdown
          class="w-full pb-4"
          v-model="selectedGroupValues"
          :options="groupDropdownOptions"
          :show-chevron="false"
          @update:modelValue="selectedGroupValues = $event"
        >
          <template #selected="{ remove }">
            <GroupBadge
              v-for="g in selectedGroupValues"
              :key="g"
              :group="g"
            />
            <span v-if="!group" class="text-sm text-gray-400">Sélectionner un groupe</span>
          </template>
          <template #option="{ option }">
            <GroupBadge :group="option.id" />
          </template>
        </MultiSelectDropdown>
        <p v-if="props.showGroupError" class="text-sm text-[var(--color-red)] mt-1">Veuillez sélectionner au moins un groupe.</p>
      </div>
    </div>
  </div>
</template>
