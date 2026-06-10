<script setup lang="ts">
import { RouterView } from 'vue-router'
import Header from '@/components/Header.vue'
</script>

<template>
  <Header
    :is-home-page="true"
    subtitle="FIL A1 2028"
  />
  <RouterView />
</template>

<style scoped></style>
