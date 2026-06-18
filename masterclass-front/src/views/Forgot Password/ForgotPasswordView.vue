<script setup lang="ts">
import Header from '@/components/Header.vue'
import { ref } from 'vue'
import { forgotPassword } from '@/services/userService'

const email = ref('')
const message = ref('')
const errorMessage = ref('')
const isSubmitting = ref(false)

const handleSubmit = async () => {
  errorMessage.value = ''
  message.value = ''
  isSubmitting.value = true
  try {
    await forgotPassword(email.value)
    message.value = 'Si un compte existe avec cet email, un lien de réinitialisation a été envoyé.'
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
      <h1 class="mb-10 text-5xl font-bold text-[var(--color-black)]">Mot de passe oublié</h1>

      <div class="w-full max-w-md">
        <form @submit.prevent="handleSubmit" class="space-y-5">
          <p class="text-sm text-gray-600">
            Entrez votre email, nous vous envoyons un lien pour réinitialiser votre mot de passe.
          </p>

          <div class="flex flex-col text-left">
            <label for="email" class="mb-1 text-sm font-medium text-gray-700">
              Email<span class="text-[var(--color-red)]">*</span>
            </label>
            <input
              v-model="email"
              id="email"
              type="email"
              required
              placeholder="Ex: yanis@en-showcase.fr"
              class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
            />
          </div>

          <p v-if="message" class="text-sm text-green-600 text-center">{{ message }}</p>
          <p v-if="errorMessage" class="text-sm text-red-500 text-center">{{ errorMessage }}</p>

          <button
            type="submit"
            :disabled="isSubmitting"
            class="w-full mt-2 rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90 disabled:opacity-50"
          >
            Envoyer le lien
          </button>

          <div class="text-center mt-5">
            <router-link
              to="/login"
              class="text-sm text-[var(--color-black)] underline hover:text-gray-600 font-medium"
            >
              Retour à la connexion
            </router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
