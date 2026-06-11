<script setup lang="ts">
import { computed } from "vue";
import { format } from "date-fns";
import { fr } from "date-fns/locale";
import { XMarkIcon } from "@heroicons/vue/24/solid";
import { CalendarIcon, ClockIcon, UserGroupIcon, MapPinIcon, TrashIcon, PencilIcon } from "@heroicons/vue/24/outline";

const props = defineProps({
  event: {
    type: Object,
    required: true,
  },
});

const isHomework = computed(() => props.event.type === "homework");

const emit = defineEmits(["close", "delete", "edit"]);

const handleDelete = () => {
  console.log("TODO: Appel API back pour supprimer l'événement", props.event);
  // emit("delete", props.event); // Pour informer le parent si besoin
};

const handleEdit = () => {
  console.log("TODO: Ouvrir la modale de modification pour l'événement", props.event);
  // emit("edit", props.event); // Pour informer le parent de l'ouverture
};

const capitalizeFirstLetter = (string: string) => {
  return string.charAt(0).toUpperCase() + string.slice(1);
};

const formattedDate = computed(() => {
  const start = props.event.start;
  const end = props.event.end;

  if (start && end) {
    const day = capitalizeFirstLetter(
      format(start, "EEEE d MMMM", { locale: fr })
    );
    const startTime = format(start, "HH:mm");
    const endTime = format(end, "HH:mm");

    return `${day} / ${startTime} - ${endTime}`;
  }

  if (start) {
    const dayTime = capitalizeFirstLetter(
      format(start, "EEEE d MMMM HH:mm", { locale: fr })
    );
    return dayTime;
  }

  return "Date non définie";
});

</script>

<template>
  <v-dialog :model-value="true" max-width="600">
    <v-card color="white" class="pa-6 rounded-lg">

      <!-- Modal header -->
      <div class="d-flex justify-space-between align-center mb-6">
        <div class="text-xl font-weight-semibold text-black">
          {{ isHomework ? "Détail du devoir" : "Détail de l'activité" }}
        </div>

        <!-- Close button -->
        <v-btn
          icon
          variant="text"
          density="comfortable"
          @click="emit('close')"
        >
          <XMarkIcon class="w-7 h-7 text-gray-600" />
        </v-btn>
      </div>

      <!-- Title + icon block -->
      <div class="d-flex align-center mb-6">
        <v-sheet
          width="50"
          height="50"
          rounded="lg"
          color="teal-lighten-5"
          class="d-flex align-center justify-center mr-4"
        >
          <CalendarIcon class="w-7 h-7 text-gray-600" />
        </v-sheet>

        <div>
          <v-chip
            size="small"
            color="deep-purple-accent-2"
            variant="tonal"
            class="font-weight-bold text-caption"
          >
            ACTIVITÉ
          </v-chip>

          <div class="text-lg font-weight-bold text-black">
            {{ props.event.title }}
          </div>
        </div>
      </div>

      <v-divider class="mb-6"></v-divider>

      <!-- Event information -->
      <div class="mb-3 d-flex align-center text-body-1 text-black">
        <ClockIcon class="w-4 h-4 text-gray-600 mr-3" />
        {{ formattedDate }}
      </div>

      <div class="mb-3 d-flex align-center text-body-1 text-black">
        <UserGroupIcon class="w-4 h-4 text-gray-600 mr-3" />
        Groupe : {{ props.event.group }}
      </div>

      <div class="mb-6 d-flex align-center text-body-1 text-black">
        <MapPinIcon class="w-4 h-4 text-gray-600 mr-3" />
        {{ props.event.location }}
      </div>

      <!-- Description box -->
      <v-sheet class="pa-4 rounded-lg bg-grey-lighten-4 mb-6 text-body-1 text-grey-darken-3">
        {{ props.event.description }}
      </v-sheet>

      <!-- Action buttons -->
      <div class="d-flex justify-space-between">
        <v-btn
          variant="tonal"
          class="text-none font-weight-regular px-4"
          elevation="0"
          style="color: rgb(220, 38, 38);"
          @click="handleDelete"
        >
          <TrashIcon class="w-5 h-5 mr-3" />
          Supprimer
        </v-btn>

        <v-btn
          variant="tonal"
          class="text-none font-weight-regular px-4 text-black"
          elevation="0"
          @click="handleEdit"
        >
          <PencilIcon class="w-5 h-5 mr-3 text-black" />
          Modifier
        </v-btn>
      </div>

    </v-card>
  </v-dialog>
</template>