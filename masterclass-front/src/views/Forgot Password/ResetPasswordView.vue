<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const token = route.query.token as string

const password = ref('')
const confirm = ref('')
const loading = ref(false)
const error = ref('')
const success = ref(false)

// --- PLACEHOLDER BACK ---
async function resetPassword(token: string, newPassword: string): Promise<{ success: boolean; message?: string }> {
  // TODO: POST /api/auth/reset-password { token, newPassword }
  await new Promise(r => setTimeout(r, 1000))
  if (!token) return { success: false, message: 'Token manquant ou expiré' }
  if (newPassword.length < 8) return { success: false, message: 'Le mot de passe doit faire au moins 8 caractères' }
  return { success: true }
}
// ------------------------

async function handleSubmit() {
  error.value = ''
  if (password.value !== confirm.value) {
    error.value = 'Les mots de passe ne correspondent pas'
    return
  }
  loading.value = true
  const res = await resetPassword(token, password.value)
  loading.value = false
  if (res.success) {
    success.value = true
    setTimeout(() => router.push('/login'), 2000)
  } else {
    error.value = res.message || 'Une erreur est survenue'
  }
}
</script>

<template>
  <main class="min-h-screen flex items-center justify-center bg-[var(--color-background)] font-[var(--font-main)]">
    <div class="flex flex-col items-center text-center w-80 gap-4">

      <template v-if="!success">
        <h1 class="text-2xl font-semibold text-[var(--color-black)] m-0">Nouveau mot de passe</h1>
        <p class="text-sm text-[var(--color-black)]/60 m-0 leading-relaxed">
          Choisissez un nouveau mot de passe pour votre compte.
        </p>

        <div class="flex flex-col items-start w-full gap-1">
          <label for="password" class="text-sm font-medium text-[var(--color-black)]">Nouveau mot de passe</label>
          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="••••••••"
            :disabled="loading"
            class="w-full px-3 py-2 border border-black/20 rounded-lg text-sm outline-none focus:border-[var(--color-primary)] transition-colors duration-200 disabled:opacity-50 placeholder:text-black/30"
          />
        </div>

        <div class="flex flex-col items-start w-full gap-1">
          <label for="confirm" class="text-sm font-medium text-[var(--color-black)]">Confirmer le mot de passe</label>
          <input
            id="confirm"
            v-model="confirm"
            type="password"
            placeholder="••••••••"
            :disabled="loading"
            class="w-full px-3 py-2 border border-black/20 rounded-lg text-sm outline-none focus:border-[var(--color-primary)] transition-colors duration-200 disabled:opacity-50 placeholder:text-black/30"
          />
        </div>

        <p v-if="error" class="text-[var(--color-red)] text-sm m-0">{{ error }}</p>

        <button
          :disabled="loading || !password || !confirm"
          @click="handleSubmit"
          class="w-full py-2.5 bg-[var(--color-primary)] text-white rounded-lg text-sm font-medium cursor-pointer transition-opacity duration-200 disabled:opacity-40 disabled:cursor-not-allowed border-none"
        >
          {{ loading ? 'Enregistrement...' : 'Enregistrer' }}
        </button>
      </template>

      <template v-else>
        <span class="text-4xl">✅</span>
        <h1 class="text-2xl font-semibold text-[var(--color-black)] m-0">Mot de passe modifié</h1>
        <p class="text-sm text-[var(--color-black)]/60 m-0">Redirection vers la connexion...</p>
      </template>

    </div>
  </main>
</template>
