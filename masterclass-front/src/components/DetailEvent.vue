<script setup lang="ts">
import { computed } from "vue";
import { format } from "date-fns";
import { fr } from "date-fns/locale";

const props = defineProps({
  event: {
    type: Object,
    required: true,
  },
});

const isHomework = computed(() => props.event.type === "homework");
</script>

<template>
  <div class="p-4 border rounded-lg space-y-4 bg-red-500">
    <h2 class="text-xl font-bold">
      {{ isHomework ? "Détails du devoir" : "Détails de l'événement" }}
    </h2>

    <div>
      <strong>{{ event.title }}</strong>
    </div>

    <div>
      <span v-if="isHomework">
        À rendre pour le
        {{ format(event.end, "EEEE d MMMM HH:mm", { locale: fr }) }}
      </span>

      <span v-else>
        {{ format(event.start, "EEEE d MMMM HH:mm", { locale: fr }) }}
      </span>
    </div>

    <div>
      Groupe : {{ event.group || "Toute la promo" }}
    </div>

    <div v-if="event.location">
      Lieu : {{ event.location }}
    </div>

    <div class="p-3 bg-gray-100 rounded">
      {{ event.description || "Aucune description fournie." }}
    </div>

    <div v-if="isHomework">
      État :
      {{ event.isDone ? "Terminé" : "À faire" }}
    </div>
  </div>
</template>