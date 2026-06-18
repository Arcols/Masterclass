<script setup lang="ts">
import { ref } from 'vue'
import MultiSelectDropdown from '@/components/MultiSelectDropdown.vue'
import GroupBadge from '@/components/GroupBadge.vue'
import mockGroups from '@/mocks/groups.json'
import Header from '@/components/Header.vue'
import { registerUser } from '@/services/userService'

const firstname = ref('')
const lastname = ref('')
const description = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const selectedGroups = ref<string[]>([])
const availableGroups = mockGroups as string[]

const loading = ref(false)
const error = ref('')
const sent = ref(false) // affiche le message "vérifiez vos mails"

const handleRegister = async () => {
  error.value = ''

  if (selectedGroups.value.length === 0) {
    error.value = 'Veuillez sélectionner au moins un groupe.'
    return
  }

  if (password.value !== confirmPassword.value) {
    error.value = 'Les mots de passe ne correspondent pas.'
    return
  }

  loading.value = true
  try {
    await registerUser({
      firstname: firstname.value,
      lastname: lastname.value,
      description: description.value,
      mail: email.value,
      password: password.value,
      confirmPassword: confirmPassword.value,
      groupIds: selectedGroups.value,
    })
    sent.value = true
  } catch (e: any) {
    error.value = e.message || 'Une erreur est survenue.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="w-full min-h-screen flex flex-col bg-white">
    <Header
      class="z-20 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-add-event-button="false"
      :show-profile="false"
      :show-navigation="false"
      subtitle="FIL A1 2028"
    />

    <div class="flex-1 flex flex-col items-center justify-center px-4 py-10">
      <!-- ── VUE CONFIRMATION ── -->
      <template v-if="sent">
        <div class="flex flex-col items-center text-center gap-4 max-w-md">
          <span class="text-5xl">✉️</span>
          <h1 class="text-3xl font-bold text-[var(--color-black)]">Vérifiez vos mails !</h1>
          <p class="text-gray-500 text-sm leading-relaxed">
            Un email de confirmation a été envoyé à <strong>{{ email }}</strong
            >.<br />
            Cliquez sur le lien dans le mail pour activer votre compte.
          </p>
          <router-link
            to="/login"
            class="text-sm text-[var(--color-black)] underline hover:text-gray-600 font-medium"
          >
            Retour à la connexion
          </router-link>
        </div>
      </template>

      <!-- ── FORMULAIRE ── -->
      <template v-else>
        <h1 class="mb-8 text-5xl font-bold text-[var(--color-black)]">Inscription</h1>

        <div class="w-full max-w-md">
          <form @submit.prevent="handleRegister" class="space-y-4">
            <div class="flex flex-col text-left">
              <label for="prenom" class="mb-1 text-sm font-medium text-gray-700">
                Prénom<span class="text-[var(--color-red)]">*</span>
              </label>
              <input
                id="prenom"
                v-model="firstname"
                type="text"
                required
                placeholder="Ex: Yanis"
                class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
              />
            </div>

            <div class="flex flex-col text-left">
              <label for="nom" class="mb-1 text-sm font-medium text-gray-700">
                Nom<span class="text-[var(--color-red)]">*</span>
              </label>
              <input
                id="nom"
                v-model="lastname"
                type="text"
                required
                placeholder="Ex: Dupont"
                class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
              />
            </div>

            <div class="flex flex-col text-left">
              <label for="description" class="mb-1 text-sm font-medium text-gray-700"
                >Description</label
              >
              <textarea
                id="description"
                v-model="description"
                rows="3"
                placeholder="Ex: je suis étudiant en FIL A1 à l'IMT Atlantique"
                class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none resize-none transition-shadow"
              ></textarea>
            </div>

            <div class="flex flex-col text-left">
              <label class="mb-1 text-sm font-medium text-gray-700">
                Groupe(s)<span class="text-[var(--color-red)]">*</span>
                <span class="text-[10px] text-[var(--color-red)] font-normal ml-1">(min. 1)</span>
              </label>
              <MultiSelectDropdown
                class="w-full"
                :options="availableGroups.map((g) => ({ id: g, label: g }))"
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
              <input
                id="email"
                v-model="email"
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
                v-model="password"
                type="password"
                required
                placeholder="••••••••••••••••••••"
                class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
              />
            </div>

            <div class="flex flex-col text-left">
              <label for="confirm_password" class="mb-1 text-sm font-medium text-gray-700">
                Confirmation du mot de passe<span class="text-[var(--color-red)]">*</span>
              </label>
              <input
                id="confirm_password"
                v-model="confirmPassword"
                type="password"
                required
                placeholder="••••••••••••••••••••"
                class="rounded-md border border-gray-300 p-2.5 text-sm placeholder-gray-400 focus:border-[var(--color-primary)] focus:ring-1 focus:ring-[var(--color-primary)] focus:outline-none transition-shadow"
              />
            </div>

            <p v-if="error" class="text-[var(--color-red)] text-sm">{{ error }}</p>

            <button
              type="submit"
              :disabled="loading"
              class="w-full mt-4 rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ loading ? 'Inscription...' : "S'inscrire" }}
            </button>

            <div class="text-center mt-5">
              <router-link
                to="/login"
                class="text-sm text-[var(--color-black)] underline hover:text-gray-600 font-medium"
              >
                Déjà un compte ?
              </router-link>
            </div>
          </form>
        </div>
      </template>
    </div>
  </div>
</template>
