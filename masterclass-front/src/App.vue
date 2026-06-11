<script setup lang="ts">
import { RouterView } from 'vue-router'
import Header from '@/components/Header.vue'
</script>

<template>
  <Header
    :show-actions="true"
    :show-profile="true"
    subtitle="FIL A1 2028"
  />
  <RouterView />
</template>

<style scoped>
</style>
