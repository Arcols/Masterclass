<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getClasses } from '@/services/classesService'
import type { Classe } from '@/types/classes'

const classes = ref<Classe[]>([])

onMounted(async () => {
  console.log('Avant chargement:', classes.value)
  classes.value = await getClasses()
  console.log('Après chargement:', classes.value)
  console.log('caca')
})

const dialog = ref(false)
const name = ref('')
const email = ref('')
const role = ref('Utilisateur')

const roles = ['Administrateur', 'Utilisateur', 'Invité']

function submit() {
  console.log({
    name: name.value,
    email: email.value,
    role: role.value,
  })
  dialog.value = false
}
</script>

<template>
  <v-btn color="primary" @click="dialog = true">
    Ouvrir le formulaire
  </v-btn>

  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>Ajouter un utilisateur</v-card-title>

      <v-card-text>
        <v-form @submit.prevent="submit">
          <v-text-field
            v-model="name"
            label="Nom"
            required
          />

          <v-text-field
            v-model="email"
            label="Email"
            type="email"
            required
          />

          <v-select
            v-model="role"
            :items="roles"
            label="Rôle"
          />
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="dialog = false">Annuler</v-btn>
        <v-btn color="primary" @click="submit">Valider</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>

</style>
