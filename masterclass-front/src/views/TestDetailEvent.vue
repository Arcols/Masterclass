<script setup lang="ts">
import { ref } from "vue";
import DetailEvent from "@/components/DetailEvent.vue";

const event = ref({
  id: 1,
  title: "Test Vue",
  type: "homework",
  start: new Date(),
  end: new Date(),
  group: "B3",
  location: "Salle 101",
  description: "Ceci est un test",
  isDone: false,
});
</script>

<template>
  <DetailEvent :event="event" />
</template>

<style scoped></style>