<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import { ref } from 'vue'
import { resetPassword } from '@/services/userService'

const route = useRoute()
const router = useRouter()
const token = route.query.token as string

const newPassword = ref('')
const confirmPassword = ref('')
const errorMessage = ref('')
const isSubmitting = ref(false)

const handleSubmit = async () => {
  errorMessage.value = ''
  isSubmitting.value = true
  try {
    await resetPassword(token, newPassword.value, confirmPassword.value)
    router.push('/login')
  } catch (e: any) {
    errorMessage.value = e.message ?? 'Une erreur est survenue'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="w-full min-h-screen flex flex-col bg-white">
    <Header
      class="z-20 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="false"
      :show-profile="false"
      subtitle="FIL A1 2028"
    />

    <div class="flex-1 flex flex-col items-center justify-center px-4 pb-20">
      <h1 class="mb-10 text-5xl font-bold text-[var(--color-black)]">Nouveau mot de passe</h1>

      <div class="w-full max-w-md">
        <form @submit.prevent="handleSubmit" class="space-y-5">
          <div class="flex flex-col text-left">
            <label for="newPassword" class="mb-1 text-sm font-medium text-gray-700">
              Nouveau mot de passe<span class="text-[var(--color-red)]">*</span>
            </label>
            <input
              v-model="newPassword"
              id="newPassword"
              type="password"
              required
              placeholder="••••••••••••••••••••"
              class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
            />
          </div>

          <div class="flex flex-col text-left">
            <label for="confirmPassword" class="mb-1 text-sm font-medium text-gray-700">
              Confirmer le mot de passe<span class="text-[var(--color-red)]">*</span>
            </label>
            <input
              v-model="confirmPassword"
              id="confirmPassword"
              type="password"
              required
              placeholder="••••••••••••••••••••"
              class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
            />
          </div>

          <p v-if="errorMessage" class="text-sm text-red-500 text-center">{{ errorMessage }}</p>

          <button
            type="submit"
            :disabled="isSubmitting"
            class="w-full mt-2 rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90 disabled:opacity-50"
          >
            Réinitialiser
          </button>
        </form>
      </div>
    </div>
  </div>
</template>
