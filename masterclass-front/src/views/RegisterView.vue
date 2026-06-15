<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import MultiSelectDropdown from '@/components/MultiSelectDropdown.vue'
import GroupBadge from '@/components/GroupBadge.vue'
import mockGroups from '@/mocks/groups.json'
import Header from '@/components/Header.vue'

const router = useRouter()

// ── ÉTAT DU FORMULAIRE ──
const selectedGroups = ref<string[]>([])

// Les groupes disponibles au choix
const availableGroups = mockGroups as string[]

const handleRegister = () => {
  // vérifier qu'au moins un groupe est sélectionné
  if (selectedGroups.value.length === 0) {
    alert('Veuillez sélectionner au moins un groupe.')
    return
  }

  console.log('Inscription avec les groupes :', selectedGroups.value)
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

    <!-- Contenu scrollable centré -->
    <div class="flex-1 flex flex-col items-center justify-center px-4 py-10">
      <h1 class="mb-8 text-5xl font-bold text-[var(--color-black)]">Inscription</h1>

      <div class="w-full max-w-md">
        <form @submit.prevent="handleRegister" class="space-y-4">

          <div class="flex flex-col text-left">
            <label for="prenom" class="mb-1 text-sm font-medium text-gray-700">
              Prénom<span class="text-[var(--color-red)]">*</span>
            </label>
            <input id="prenom" type="text" required placeholder="Ex: yanis@en-showcase.fr" class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow" />
          </div>

          <div class="flex flex-col text-left">
            <label for="nom" class="mb-1 text-sm font-medium text-gray-700">
              Nom<span class="text-[var(--color-red)]">*</span>
            </label>
            <input id="nom" type="text" required placeholder="Ex: yanis@en-showcase.fr" class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow" />
          </div>

          <div class="flex flex-col text-left">
            <label for="description" class="mb-1 text-sm font-medium text-gray-700">Description</label>
            <textarea id="description" rows="3" placeholder="Ex: je suis étudiant en FIL A1 à l'IMT Atlantique" class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none resize-none transition-shadow"></textarea>
          </div>

          <!-- ── CHAMP GROUPE DYNAMIQUE ── -->
          <div class="flex flex-col text-left">
            <label class="mb-1 text-sm font-medium text-gray-700">
              Groupe(s)<span class="text-[var(--color-red)]">*</span><span class="text-[10px] text-[var(--color-red)] font-normal ml-1">(min. 1)</span>
            </label>

            <MultiSelectDropdown
              class="w-full"
              :options="availableGroups.map(g => ({ id: g, label: g }))"
              :modelValue="selectedGroups"
              @update:modelValue="selectedGroups = $event"
            >
              <template #selected="{ remove }">
                <GroupBadge
                  v-for="g in selectedGroups"
                  :key="g"
                  :group="g"
                  removable
                  @remove="remove(g)"
                />
              </template>
              <template #option="{ option }">
                <GroupBadge :group="option.id" />
              </template>
            </MultiSelectDropdown>
          </div>

          <div class="flex flex-col text-left">
            <label for="email" class="mb-1 text-sm font-medium text-gray-700">
              Email<span class="text-[var(--color-red)]">*</span>
            </label>
            <input id="email" type="email" required placeholder="Ex: yanis@en-showcase.fr" class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow" />
          </div>

          <div class="flex flex-col text-left">
            <label for="password" class="mb-1 text-sm font-medium text-gray-700">
              Mot de passe<span class="text-[var(--color-red)]">*</span>
            </label>
            <input id="password" type="password" required placeholder="••••••••••••••••••••" class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow" />
          </div>

          <div class="flex flex-col text-left">
            <label for="confirm_password" class="mb-1 text-sm font-medium text-gray-700">
              Confirmation du mot de passe<span class="text-[var(--color-red)]">*</span>
            </label>
            <input id="confirm_password" type="password" required placeholder="••••••••••••••••••••" class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow" />
          </div>

          <button type="submit" class="w-full mt-4 rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90">
            S'inscrire
          </button>

          <div class="text-center mt-5">
            <router-link to="/login" class="text-sm text-[var(--color-black)] underline hover:text-gray-600 font-medium">
              Déjà un compte ?
            </router-link>
          </div>

        </form>
      </div>
    </div>
  </div>
</template>
