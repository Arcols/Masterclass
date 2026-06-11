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
</script>

<template>

</template>

<style scoped>

</style>
