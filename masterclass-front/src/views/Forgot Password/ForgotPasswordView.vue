<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')
const loading = ref(false)
const error = ref('')
const sent = ref(false)

// --- PLACEHOLDER BACK ---
async function sendResetEmail(email: string): Promise<{ success: boolean; message?: string }> {
  // TODO: POST /api/auth/forgot-password { email }
  await new Promise(r => setTimeout(r, 1000))
  if (!email.includes('@')) return { success: false, message: 'Email invalide' }
  return { success: true }
}

/**
 * Le back envoie un mail avec un lien genre /reset-password?token=abc123.
 * Côté front tu n'as rien à faire de spécial, l'utilisateur clique sur le lien et arrive directement sur ResetPassword.vue
 * avec le token dans l'URL. C'est ce qu'on a déjà prévu avec route.query.token.
 */

// ------------------------

async function handleSubmit() {
  error.value = ''
  loading.value = true
  const res = await sendResetEmail(email.value)
  loading.value = false
  if (res.success) sent.value = true
  else error.value = res.message || 'Une erreur est survenue'
}
</script>

<template>
  <main class="min-h-screen flex items-center justify-center bg-[var(--color-background)] font-[var(--font-main)]">
    <div class="flex flex-col items-center text-center w-80 gap-4">

      <template v-if="!sent">
        <h1 class="text-2xl font-semibold text-[var(--color-black)] m-0">Mot de passe oublié</h1>
        <p class="text-sm text-[var(--color-black)]/60 m-0 leading-relaxed">
          Renseignez votre email, vous recevrez un lien de réinitialisation.
        </p>

        <div class="flex flex-col items-start w-full gap-1">
          <label for="email" class="text-sm font-medium text-[var(--color-black)]">Email</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="••••••@imt-atlantique.net"
            :disabled="loading"
            class="w-full px-3 py-2 border border-black/20 rounded-lg text-sm outline-none focus:border-[var(--color-primary)] transition-colors duration-200 disabled:opacity-50 placeholder:text-black/30"
          />
        </div>

        <p v-if="error" class="text-[var(--color-red)] text-sm m-0">{{ error }}</p>

        <button
          :disabled="loading || !email"
          @click="handleSubmit"
          class="w-full py-2.5 bg-[var(--color-primary)] text-white rounded-lg text-sm font-medium cursor-pointer transition-opacity duration-200 disabled:opacity-40 disabled:cursor-not-allowed border-none"
        >
          {{ loading ? 'Envoi...' : 'Envoyer le lien' }}
        </button>

        <a
          href="/login"
          class="text-sm text-[var(--color-black)]/50 no-underline cursor-pointer transition-colors duration-200 hover:text-[var(--color-primary)]"
        >
        ← Retour à la connexion
        </a>
      </template>

      <template v-else>
        <span class="text-4xl">✉️</span>
        <h1 class="text-2xl font-semibold text-[var(--color-black)] m-0">Email envoyé</h1>
        <p class="text-sm text-[var(--color-black)]/60 m-0 leading-relaxed">
          Un lien a été envoyé à <strong>{{ email }}</strong>. Vérifiez votre boîte mail.
        </p>
        <a
          href="/login"
          class="text-sm text-[var(--color-black)]/50 no-underline cursor-pointer transition-colors duration-200 hover:text-[var(--color-primary)]"
        >
        ← Retour à la connexion
      </a>
      </template>

    </div>
  </main>
</template>
