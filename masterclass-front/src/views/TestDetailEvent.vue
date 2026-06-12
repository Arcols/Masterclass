<script setup lang="ts">
import { ref } from "vue";
import DetailEventModal from "@/components/DetailEventModal.vue";

const event = ref({
  id: 1,
  title: "Showcase Yanis",
  type: "devoir",
  start: new Date("2026-06-11T14:00:00"),
  end: new Date("2026-06-11T16:30:00"),
  group: "FIL A1",
  location: "MDE",
  description: "Préparez vous pour l’évent de l’année organisé par votre rappeur préféré (d’après une étude réalisée sur une personne). Ne ratez pas le concert privé, happening garantis.",
  isDone: false,
});

const isOpen = ref(true);
</script>

<template>
  <DetailEventModal v-if="isOpen" :event="event" @close="isOpen = false" />
</template>
