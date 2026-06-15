<script setup lang="ts">
import { ref } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import { UserIcon, ArrowRightStartOnRectangleIcon } from '@heroicons/vue/24/outline';

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
}>();

const router = useRouter();
const isProfileMenuOpen = ref(false);

const toggleProfileMenu = () => {
  isProfileMenuOpen.value = !isProfileMenuOpen.value;
};

const goToProfile = () => {
  isProfileMenuOpen.value = false;
  router.push('/profile');
};

const logout = () => {
  isProfileMenuOpen.value = false;
  router.push('/login');
};
</script>

<template>
  <header class="flex w-full items-center justify-between px-3 py-1 md:pr-6 md:pl-3 md:py-2 relative bg-white">

    <RouterLink
      to="/"
      class="flex items-center gap-2 md:gap-3 py-1.5 md:py-2 px-2 md:px-3 rounded-md cursor-pointer transition-colors duration-150 hover:bg-[var(--color-primary)]/10"
    >
      <img
        src="@/assets/logo.svg"
        alt="Logo Masterclass"
        class="w-8 h-8 md:w-11 md:h-11 object-contain"
      />

      <div class="flex flex-col">
        <span class="text-xl md:text-2xl font-bold leading-tight">Masterclass</span>
        <span v-if="subtitle" class="text-[11px] md:text-[13px] text-gray-500 leading-none md:mt-0.5">
          {{ subtitle }}
        </span>
      </div>
    </RouterLink>

    <div v-if="showActions || showProfile" class="flex items-center gap-2.5 md:gap-5">

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
          class="inline-flex items-center justify-center w-8 h-8 md:w-9 md:h-9 rounded-full border-2 border-[var(--color-primary)] text-[var(--color-primary)] bg-transparent cursor-pointer transition-colors duration-150 hover:bg-[#00786f]/10"
          aria-label="Historique"
          @click="emit('open-history')"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 md:w-[22px] md:h-[22px]" viewBox="0 0 256 256" fill="currentColor" aria-hidden="true">
            <path d="M232,128A104,104,0,1,1,128,24,104.11,104.11,0,0,1,232,128Zm-24,0a80,80,0,1,0-80,80A80.09,80.09,0,0,0,208,128Zm-72,0V80a12,12,0,0,0-24,0v52a12,12,0,0,0,5.37,10l32,21.34a12,12,0,1,0,13.26-20Z" />
          </svg>
        </button>
      </template>

      <div v-if="showProfile" class="relative">
        <div
          v-if="isProfileMenuOpen"
          class="fixed inset-0 z-40"
          @click="isProfileMenuOpen = false"
        ></div>

        <button
          type="button"
          class="relative z-50 inline-flex items-center justify-center p-0 border-[2.5px] border-[var(--color-primary)] rounded-full bg-transparent cursor-pointer hover:opacity-80 transition-opacity focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[var(--color-primary)]"
          @click="toggleProfileMenu"
        >
          <img
            src="@/assets/avatar-placeholder.svg"
            alt="" aria-hidden="true"
            class="w-8 h-8 md:w-9 md:h-9 rounded-full object-cover bg-white"
          />
        </button>

        <div
          v-if="isProfileMenuOpen"
          class="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-xl py-1 border border-gray-200 z-[100] animate-fade-in-up origin-top-right"
        >
          <button
            @click="goToProfile"
            class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 flex items-center gap-2 transition-colors"
          >
            <UserIcon class="w-4 h-4" />
            Mon Profil
          </button>

          <div class="border-t border-gray-100 my-1"></div>

          <button
            @click="logout"
            class="w-full text-left px-4 py-2 text-sm text-[var(--color-red)] hover:bg-red-50 flex items-center gap-2 transition-colors"
          >
            <ArrowRightStartOnRectangleIcon class="w-4 h-4" />
            Se déconnecter
          </button>
        </div>
      </div>

    </div>
  </header>
</template>

<style scoped>
.animate-fade-in-up {
  animation: fadeInUp 0.15s ease-out forwards;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(5px) scale(0.98); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}
</style>
