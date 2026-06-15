<script setup lang="ts">
import { useRouter } from 'vue-router'
import Header from '@/components/Header.vue'

const router = useRouter()

const handleLogin = () => {
  // Redirection vers le planning
  router.push('/')
}
</script>

<template>
  <div class="w-full min-h-screen flex flex-col bg-white">

    <!-- Header (juste partie gauche) -->
    <Header
      class="z-20 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="false"
      :show-profile="false"
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
              id="password"
              type="password"
              required
              placeholder="••••••••••••••••••••"
              class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
            />
          </div>

          <button
            type="submit"
            class="w-full mt-2 rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90"
          >
            Se connecter
          </button>

          <div class="text-center mt-5">
            <router-link to="/register" class="text-sm text-[var(--color-black)] underline hover:text-gray-600 font-medium">
              Pas encore de compte ?
            </router-link>
          </div>

        </form>
      </div>
    </div>
  </div>
</template>
