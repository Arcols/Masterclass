<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { PencilIcon } from '@heroicons/vue/24/outline'
import Header from '@/components/Header.vue'
import ChangePasswordModal from '@/components/modals/ChangePasswordModal.vue'
import GroupBadge from '@/components/GroupBadge.vue'
import MultiSelectDropdown from '@/components/MultiSelectDropdown.vue'
import { getUserById, updateUserById } from '@/services/userService.ts'

interface UserProfileData {
  firstName?: string
  lastName?: string
  email?: string
  description?: string
  groups?: string[]
}

interface BackendUserResponse {
  useFirstname: string
  useLastname: string
  useMail: string
  useDescription: string
  groups: { groId: string }[]
}

const userProfile = ref<UserProfileData>({})
const userForm = ref<UserProfileData>({})
const availableGroups = ref<string[]>([])
const isEditing = ref(false)
const showPasswordModal = ref(false)

onMounted(async () => {
  const userId = 'U1' // TODO : À dynamiser plus tard
  try {
    const rawData = await getUserById(userId)
    if (rawData) {
      const data = JSON.parse(rawData) as BackendUserResponse

      userProfile.value = {
        firstName: data.useFirstname,
        lastName: data.useLastname,
        email: data.useMail,
        description: data.useDescription,
        groups: data.groups ? data.groups.map((g) => g.groId) : [],
      }
    } else {
      throw new Error("Réponse de l'API vide")
    }
  } catch (error) {
    console.warn('Erreur de récupération du profil :', error)
  }
})

const startEditing = () => {
  userForm.value = { ...userProfile.value }
  isEditing.value = true
}

const cancelEditing = () => {
  isEditing.value = false
}

const saveProfile = async () => {
  // Vérification de l'existence avant de lire la longueur
  if (!userForm.value.groups || userForm.value.groups.length === 0) {
    alert('Veuillez sélectionner au moins un groupe.')
    return
  }

  try {
    const userId = 'U1' // TODO : À dynamiser plus tard

    const payload = {
      useFirstname: userForm.value.firstName,
      useLastname: userForm.value.lastName,
      useMail: userForm.value.email,
      useDescription: userForm.value.description,
      // Utilisation d'une valeur de repli (fallback) pour rassurer TypeScript
      groups: (userForm.value.groups || []).map((g) => ({ groId: g })),
    }

    await updateUserById(userId, payload)

    userProfile.value = { ...userForm.value }
    isEditing.value = false
    console.log('Profil sauvegardé en base de données avec succès.')
  } catch (error) {
    console.error('Erreur lors de la sauvegarde :', error)
    alert('Une erreur est survenue lors de la mise à jour du profil.')
  }
}

const handlePasswordChange = (payload: { current: string; new: string }) => {
  console.log('Demande de changement de mot de passe avec :', payload)
  // Ici, tu appelleras ton API plus tard.
  alert('Mot de passe mis à jour avec succès !')
  showPasswordModal.value = false
}
</script>

<template>
  <div class="w-full min-h-screen flex flex-col bg-white relative">
    <Header
      class="z-50 bg-white border-b border-gray-100 shrink-0"
      :show-actions="false"
      :show-profile="true"
      subtitle="FIL A1 2028"
    />

    <main class="flex-1 flex flex-col items-center py-10 px-4 overflow-y-auto">
      <div class="flex flex-col items-center">
        <img
          src="@/assets/avatar-placeholder.svg"
          alt="Avatar"
          class="w-32 h-32 rounded-full object-cover border border-gray-200 shadow-sm"
        />
        <h1 class="text-3xl font-bold text-[var(--color-black)] mt-4">
          {{ userProfile.firstName }} {{ userProfile.lastName }}
        </h1>
      </div>

      <div v-if="!isEditing" class="w-full max-w-md mt-10 space-y-6">
        <div>
          <p class="text-xs font-medium text-[var(--color-primary)]">Prénom</p>
          <p class="text-gray-800 text-sm mt-0.5">{{ userProfile.firstName }}</p>
        </div>

        <div>
          <p class="text-xs font-medium text-[var(--color-primary)]">Nom</p>
          <p class="text-gray-800 text-sm mt-0.5">{{ userProfile.lastName }}</p>
        </div>

        <div>
          <p class="text-xs font-medium text-[var(--color-primary)]">Description</p>
          <p class="text-gray-800 text-sm mt-0.5 leading-relaxed">
            {{ userProfile.description || 'Aucune description.' }}
          </p>
        </div>

        <div>
          <p class="text-xs font-medium text-[var(--color-primary)] mb-1.5">Groupe(s)</p>
          <div class="flex flex-wrap gap-2">
            <GroupBadge v-for="g in userProfile.groups" :key="g" :group="g" />
          </div>
        </div>

        <div>
          <p class="text-xs font-medium text-[var(--color-primary)]">Email</p>
          <p class="text-gray-800 text-sm mt-0.5 underline decoration-gray-300 underline-offset-2">
            {{ userProfile.email }}
          </p>
        </div>

        <div class="flex items-center justify-between border-t border-gray-100 pt-4 mt-4">
          <div>
            <p class="text-xs font-medium text-[var(--color-primary)]">Mot de passe</p>
            <p class="text-gray-500 text-sm mt-0.5 tracking-widest">*************</p>
          </div>
          <button
            @click="showPasswordModal = true"
            class="text-xs font-medium text-[var(--color-primary)] hover:underline cursor-pointer"
          >
            Modifier
          </button>
        </div>

        <div class="pt-6">
          <button
            @click="startEditing"
            class="w-full flex items-center justify-center gap-2 rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90 cursor-pointer"
          >
            <PencilIcon class="w-5 h-5" />
            Modifier mon profil
          </button>
        </div>
      </div>

      <form v-else @submit.prevent="saveProfile" class="w-full max-w-md mt-8 space-y-4">
        <div class="flex flex-col text-left">
          <label class="mb-1 text-xs font-medium text-gray-700">
            Prénom<span class="text-[var(--color-red)]">*</span>
          </label>
          <input
            v-model="userForm.firstName"
            type="text"
            required
            class="rounded-md border border-gray-300 p-2 text-sm focus:border-[var(--color-primary)] focus:outline-none"
          />
        </div>

        <div class="flex flex-col text-left">
          <label class="mb-1 text-xs font-medium text-gray-700">
            Nom<span class="text-[var(--color-red)]">*</span>
          </label>
          <input
            v-model="userForm.lastName"
            type="text"
            required
            class="rounded-md border border-gray-300 p-2 text-sm focus:border-[var(--color-primary)] focus:outline-none"
          />
        </div>

        <div class="flex flex-col text-left">
          <label class="mb-1 text-xs font-medium text-gray-700">Description</label>
          <textarea
            v-model="userForm.description"
            rows="3"
            class="rounded-md border border-gray-300 p-2 text-sm focus:border-[var(--color-primary)] focus:outline-none resize-none"
          ></textarea>
        </div>

        <div class="flex flex-col text-left">
          <label class="mb-1 text-xs font-medium text-gray-700">
            Groupe(s)<span class="text-[var(--color-red)]">*</span
            ><span class="text-[10px] text-[var(--color-red)] font-normal ml-1">(min. 1)</span>
          </label>
          <MultiSelectDropdown
            class="w-full"
            :options="availableGroups.map((g) => ({ id: g, label: g }))"
            :modelValue="userForm.groups"
            @update:modelValue="userForm.groups = $event"
          >
            <template #selected="{ remove }">
              <GroupBadge
                v-for="g in userForm.groups"
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
          <label class="mb-1 text-xs font-medium text-gray-700">
            Email<span class="text-[var(--color-red)]">*</span>
          </label>
          <input
            v-model="userForm.email"
            type="email"
            required
            class="rounded-md border border-gray-300 p-2 text-sm focus:border-[var(--color-primary)] focus:outline-none"
          />
        </div>

        <div class="pt-6">
          <button
            type="submit"
            class="w-full rounded-md bg-[var(--color-primary)] px-5 py-2.5 text-white font-medium transition hover:opacity-90 cursor-pointer"
          >
            Sauvegarder les modifications
          </button>

          <div class="text-center mt-3">
            <button
              type="button"
              @click="cancelEditing"
              class="text-sm text-[var(--color-black)] underline hover:text-gray-600 font-medium cursor-pointer"
            >
              Annuler
            </button>
          </div>
        </div>
      </form>
    </main>

    <ChangePasswordModal
      v-if="showPasswordModal"
      @close="showPasswordModal = false"
      @save="handlePasswordChange"
    />
  </div>
</template>
