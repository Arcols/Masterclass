<script setup lang="ts">
import Header from '@/components/Header.vue'
import PlanningBoard from '@/components/PlanningBoard.vue'
</script>

<template>
  <div class="w-full h-screen flex flex-col bg-[var(--color-background)] overflow-hidden">
    <Header
      class="z-20 bg-[var(--color-background)] shadow-sm shrink-0"
      :show-actions="true"
      :show-profile="true"
      subtitle="FIL A1 2028"
    />

    <main class="flex-1 flex flex-col p-0 md:p-6 lg:p-8 min-h-0">
      <PlanningBoard class="flex-1 min-h-0" />
    </main>
  </div>
</template>
