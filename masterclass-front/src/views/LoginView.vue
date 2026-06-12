<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>
<script setup lang="ts"></script>

<template>
  <div class="flex min-h-screen flex-col items-center justify-center text-center">
    <h1 class="mb-8 text-5xl font-bold">Connexion</h1>

    <div class="w-full max-w-sm">
      <form class="space-y-4">
        <div class="flex flex-col text-left">
          <label for="email" class="mb-1 font-medium"> Email </label>
          <input
            id="email"
            type="email"
            placeholder="Ex: name.surname@imt-atlantique.net"
            class="rounded-md border border-gray-300 p-2.5 placeholder-gray-400 focus:border-teal-600 focus:outline-none"
          />
        </div>

        <div class="flex flex-col text-left">
          <label for="password" class="mb-1 font-medium"> Password </label>
          <input
            id="password"
            type="password"
            placeholder="••••••••••••••••••••"
            class="rounded-md border border-gray-300 p-2.5 placeholder-gray-400 focus:border-teal-600 focus:outline-none"
          />
        </div>

        <RouterLink
          to="/"
          class="block w-full rounded-md bg-teal-700 px-5 py-2.5 text-white transition hover:bg-teal-800"
        >
          Login
        </RouterLink>
      </form>
    </div>
  </div>
</template>
