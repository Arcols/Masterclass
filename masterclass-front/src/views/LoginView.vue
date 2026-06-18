<script setup lang="ts">
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'
import { ref } from 'vue'
import { login } from '@/services/userService'
import { onMounted } from 'vue'
import { useAuth } from '@/utils/checkingAuth'

const { requireAuth } = useAuth()

onMounted(async () => {
  await requireAuth() // redirige vers /login si token invalide
})

const router = useRouter()
const email = ref('')
const password = ref('')
const errorMessage = ref('')

const handleLogin = async () => {
  try {
    await login(email.value, password.value)
    router.push('/')
  } catch (e: any) {
    errorMessage.value = e.message
  }
}
</script>

<template>
  <div class="w-full min-h-screen flex flex-col bg-white">
    <!-- Header (juste partie gauche) -->
    <Header
      class="z-20 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-add-event-button="false"
      :show-profile="false"
      :show-navigation="false"
      subtitle="FIL A1 2028"
    />

    <!-- Contenu centré -->
    <div class="flex-1 flex flex-col items-center justify-center px-4 pb-20">
      <h1 class="mb-10 text-5xl font-bold text-[var(--color-black)]">Connexion</h1>

      <div class="w-full max-w-md">
        <form @submit.prevent="handleLogin" class="space-y-5">
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

          <div class="flex flex-col text-left">
            <label for="password" class="mb-1 text-sm font-medium text-gray-700">
              Mot de passe<span class="text-[var(--color-red)]">*</span>
            </label>
            <input
              v-model="password"
              id="password"
              type="password"
              required
              placeholder="••••••••••••••••••••"
              class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
            />
            <a class="text-sm text-[var(--color-black)] underline hover:text-[var(--color-primary)] font-medium transition-colors duration-500 cursor-pointer" href="/forgot-password">
              Mot de passe oublié ?
            </a>
          </div>

          <p v-if="errorMessage" class="text-sm text-red-500 text-center">
            {{ errorMessage }}
          </p>

          <button
            type="submit"
            class="w-full mt-2 rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90"
          >
            Se connecter
          </button>

          <div class="text-center mt-5">
            <router-link
              to="/register"
              class="text-sm text-[var(--color-black)] underline hover:text-gray-600 font-medium"
            >
              Pas encore de compte ?
            </router-link>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
