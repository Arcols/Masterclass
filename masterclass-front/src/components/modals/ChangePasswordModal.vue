<script setup lang="ts">
import { ref } from 'vue';
import { XMarkIcon } from '@heroicons/vue/24/outline';

const emit = defineEmits(['close', 'save']);

const currentPassword = ref('');
const newPassword = ref('');
const confirmPassword = ref('');
const errorMessage = ref('');

const handleSubmit = () => {
  errorMessage.value = '';

  if (!currentPassword.value || !newPassword.value || !confirmPassword.value) {
    errorMessage.value = 'Veuillez remplir tous les champs.';
    return;
  }

  if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = 'Les nouveaux mots de passe ne correspondent pas.';
    return;
  }

  if (newPassword.value.length < 6) {
    errorMessage.value = 'Le nouveau mot de passe doit faire au moins 6 caractères.';
    return;
  }

  // Si tout est bon, on remonte l'info au parent
  emit('save', {
    current: currentPassword.value,
    new: newPassword.value
  });
};
</script>

<template>
  <div class="fixed inset-0 z-[100] flex items-center justify-center bg-black/40 p-4 backdrop-blur-sm transition-opacity">
    <div class="bg-white w-full max-w-md rounded-xl shadow-xl flex flex-col overflow-hidden">

      <div class="flex items-center justify-between p-4 border-b border-gray-100">
        <h2 class="text-lg font-bold text-[var(--color-black)]">Modifier le mot de passe</h2>
        <button @click="emit('close')" class="p-1 rounded-full hover:bg-gray-100 transition-colors">
          <XMarkIcon class="w-5 h-5 text-gray-500" />
        </button>
      </div>

      <form @submit.prevent="handleSubmit" class="p-5 space-y-4">

        <div v-if="errorMessage" class="p-3 bg-red-50 text-[var(--color-red)] text-sm rounded-md border border-red-100">
          {{ errorMessage }}
        </div>

        <div class="flex flex-col text-left">
          <label class="mb-1 text-xs font-medium text-gray-700">Mot de passe actuel</label>
          <input
            v-model="currentPassword"
            type="password"
            class="rounded-md border border-gray-300 p-2 text-sm focus:border-[var(--color-primary)] focus:outline-none"
          />
        </div>

        <div class="flex flex-col text-left">
          <label class="mb-1 text-xs font-medium text-gray-700">Nouveau mot de passe</label>
          <input
            v-model="newPassword"
            type="password"
            class="rounded-md border border-gray-300 p-2 text-sm focus:border-[var(--color-primary)] focus:outline-none"
          />
        </div>

        <div class="flex flex-col text-left">
          <label class="mb-1 text-xs font-medium text-gray-700">Confirmer le nouveau mot de passe</label>
          <input
            v-model="confirmPassword"
            type="password"
            class="rounded-md border border-gray-300 p-2 text-sm focus:border-[var(--color-primary)] focus:outline-none"
          />
        </div>

        <div class="pt-4 flex gap-3">
          <button
            type="button"
            @click="emit('close')"
            class="flex-1 rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
          >
            Annuler
          </button>
          <button
            type="submit"
            class="flex-1 rounded-md bg-[var(--color-primary)] px-4 py-2 text-sm font-medium text-white transition hover:opacity-90"
          >
            Valider
          </button>
        </div>
      </form>

    </div>
  </div>
</template>
