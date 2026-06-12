<script setup lang="ts">
import { ref, computed } from 'vue';
import { ChevronDownIcon } from '@heroicons/vue/24/outline';

const props = defineProps<{
  modelValue: string[];
  options: { id: string; label: string }[];
}>();

const emit = defineEmits(['update:modelValue']);

const isOpen = ref(false);

const toggle = () => (isOpen.value = !isOpen.value);

const selectOption = (id: string) => {
  if (!props.modelValue.includes(id)) {
    emit('update:modelValue', [...props.modelValue, id]);
  }
  isOpen.value = false;
};

const removeOption = (id: string) => {
  emit('update:modelValue', props.modelValue.filter((val) => val !== id));
};

// Ne montre dans le menu que les options qui ne sont pas encore sélectionnées
const availableOptions = computed(() =>
  props.options.filter((o) => !props.modelValue.includes(o.id))
);
</script>

<template>
  <div class="relative inline-block min-w-[160px]">
    <div v-if="isOpen" class="fixed inset-0 z-40" @click="isOpen = false"></div>

    <div
      class="relative z-50 flex items-center min-h-[36px] bg-white border border-gray-300 rounded-md px-2 py-1 gap-2 cursor-pointer hover:border-gray-400 transition-colors"
      @click="toggle"
    >
      <div class="flex flex-wrap gap-1 flex-1 items-center">
        <slot name="selected" :remove="removeOption"></slot>
      </div>
      <ChevronDownIcon class="w-4 h-4 text-gray-500 shrink-0" />
    </div>

    <div
      v-if="isOpen && availableOptions.length > 0"
      class="absolute z-50 w-full mt-1 bg-white border border-gray-200 rounded-md shadow-lg py-1 max-h-48 overflow-y-auto"
    >
      <div
        v-for="opt in availableOptions"
        :key="opt.id"
        @click.stop="selectOption(opt.id)"
        class="px-3 py-2 hover:bg-gray-50 cursor-pointer text-sm"
      >
        <slot name="option" :option="opt">{{ opt.label }}</slot>
      </div>
    </div>
  </div>
</template>
