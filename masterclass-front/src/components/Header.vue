<script setup lang="ts">
import { RouterLink } from 'vue-router';

const props = withDefaults(
  defineProps<{
    showActions?: boolean;
    showProfile?: boolean;
    subtitle?: string;
  }>(),
  {
    showActions: false,
    showProfile: true,
    subtitle: '',
  }
);

const emit = defineEmits<{
  (e: 'add-event'): void;
  (e: 'open-history'): void;
  (e: 'open-profile'): void;
}>();
</script>

<template>
  <header class="flex w-full items-center justify-between pt-4 pr-6 pl-3">

    <RouterLink
      to="/"
      class="flex items-center gap-3 py-2 px-3 rounded-md cursor-pointer transition-colors duration-150 hover:bg-[var(--color-primary)]/10"
    >
      <img
        src="@/assets/logo.svg"
        alt="Logo Masterclass"
        class="w-11 h-11 object-contain"
      />

      <div class="flex flex-col">
        <span class="text-2xl font-bold leading-tight">Masterclass</span>
        <span v-if="subtitle" class="text-[13px] text-gray-500">{{ subtitle }}</span>
      </div>
    </RouterLink>

    <div v-if="showActions || showProfile" class="flex items-center gap-5">

      <template v-if="showActions">
        <button
          type="button"
          class="inline-flex items-center gap-2 px-4 py-2 rounded-md bg-[var(--color-primary)] text-white text-sm font-semibold cursor-pointer transition-colors duration-150 whitespace-nowrap hover:bg-[#006059] max-md:w-9 max-md:h-9 max-md:p-0 max-md:rounded-full max-md:justify-center"
          @click="emit('add-event')"
          aria-label="Ajouter un événement"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 256 256" fill="currentColor" aria-hidden="true">
            <path d="M228,128a12,12,0,0,1-12,12H140v76a12,12,0,0,1-24,0V140H40a12,12,0,0,1,0-24h76V40a12,12,0,0,1,24,0v76h76A12,12,0,0,1,228,128Z" />
          </svg>
          <span class="max-md:hidden">Ajouter un événement</span>
        </button>

        <button
          type="button"
          class="inline-flex items-center justify-center w-9 h-9 rounded-full border-2 border-[var(--color-primary)] text-[var(--color-primary)] bg-transparent cursor-pointer transition-colors duration-150 hover:bg-[#00786f]/10"
          aria-label="Historique"
          @click="emit('open-history')"
        >
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 256 256" fill="currentColor" aria-hidden="true">
            <path d="M232,128A104,104,0,1,1,128,24,104.11,104.11,0,0,1,232,128Zm-24,0a80,80,0,1,0-80,80A80.09,80.09,0,0,0,208,128Zm-72,0V80a12,12,0,0,0-24,0v52a12,12,0,0,0,5.37,10l32,21.34a12,12,0,1,0,13.26-20Z" />
          </svg>
        </button>
      </template>

      <button
        v-if="showProfile"
        type="button"
        class="inline-flex items-center justify-center p-0 border-[2.5px] border-[var(--color-primary)] rounded-full bg-transparent cursor-pointer hover:opacity-80 transition-opacity"
        aria-label="Profil"
        @click="emit('open-profile')"
      >
        <img
          src="@/assets/avatar-placeholder.svg"
          alt="" aria-hidden="true"
          class="w-9 h-9 rounded-full object-cover"
        />
      </button>
    </div>
  </header>
</template>
