<script setup lang="ts">
import { computed } from "vue";
import { RouterLink } from "vue-router";

// ── Props ──────────────────────────────────────────────────────────────────
const props = withDefaults(
  defineProps<{
    /** Affiche le bouton "Ajouter" et "Historique" uniquement sur la page Accueil */
    isHomePage?: boolean;
    /** Considère l'écran suffisamment large pour afficher les boutons supplémentaires */
    isDesktop?: boolean;
    /** Libellé additionnel sous le titre (ex : "FIL 2028") */
    subtitle?: string;
  }>(),
  {
    isHomePage: false,
    isDesktop: true,
    subtitle: "",
  }
);

// ── Emits ──────────────────────────────────────────────────────────────────
const emit = defineEmits<{
  (e: "add-event"): void;
  (e: "open-history"): void;
  (e: "open-profile"): void;
}>();

// Only show the extra widgets when both conditions are met
const showExtras = computed(() => props.isHomePage && props.isDesktop);
</script>

<template>
  <header class="app-header">
    <!-- ── Logo / titre ── -->
    <RouterLink to="/" class="brand">
      <img src="@/assets/logo.svg" height="45" alt="Logo Masterclass" class="brand__icon" />

      <div class="brand__text">
        <span class="brand__name">Masterclass</span>
        <span class="brand__subtitle">{{ subtitle }}</span>
      </div>
    </RouterLink>

    <!-- ── Actions (uniquement Accueil + desktop) ── -->
    <div v-if="showExtras" class="header-actions">
      <!-- Bouton Ajouter un événement -->
      <button class="btn-add" @click="emit('add-event')">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 256 256" fill="currentColor" aria-hidden="true">
          <path d="M228,128a12,12,0,0,1-12,12H140v76a12,12,0,0,1-24,0V140H40a12,12,0,0,1,0-24h76V40a12,12,0,0,1,24,0v76h76A12,12,0,0,1,228,128Z"/>
        </svg>
        Ajouter un événement
      </button>

      <!-- Historique -->
      <button class="btn-icon" aria-label="Historique" @click="emit('open-history')">
        <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 256 256" fill="currentColor" aria-hidden="true">
          <path d="M232,128A104,104,0,1,1,128,24,104.11,104.11,0,0,1,232,128Zm-24,0a80,80,0,1,0-80,80A80.09,80.09,0,0,0,208,128Zm-72,0V80a12,12,0,0,0-24,0v52a12,12,0,0,0,5.37,10l32,21.34a12,12,0,1,0,13.26-20Z"/>
        </svg>
      </button>

      <!-- Avatar utilisateur -->
      <button class="btn-avatar" aria-label="Profil" @click="emit('open-profile')">
        <img src="@/assets/avatar-placeholder.svg" alt="Avatar" class="avatar__img" />
      </button>
    </div>
  </header>
</template>

<style scoped>
/* ── Layout ──────────────────────────────────────────────────────────────── */
.app-header {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.5rem 0 0.75rem;
  font-family: inherit;
}

/* ── Brand ───────────────────────────────────────────────────────────────── */
.brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.25rem 0.75rem;
  border-radius: 0.375rem;
  text-decoration: none;
  color: inherit;
  transition: background-color 0.15s ease;
}
.brand:hover {
  background-color: color-mix(in srgb, var(--color-primary) 12%, transparent);
}

.brand__icon {
  flex-shrink: 0;
  color: currentColor;
}

.brand__text {
  display: flex;
  flex-direction: column;
}

.brand__name {
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 1.2;
  letter-spacing: -0.01em;
}

.brand__subtitle {
  font-size: 0.8125rem;
  color: #6b7280;
  line-height: 1.3;
}

/* ── Right-side actions ──────────────────────────────────────────────────── */
.header-actions {
  display: flex;
  align-items: center;
  gap: 1.25rem;
}

/* ── Add-event button ────────────────────────────────────────────────────── */
.btn-add {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  border: none;
  background-color: var(--color-primary);
  color: #fff;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.15s ease, box-shadow 0.15s ease;
  white-space: nowrap;
}
.btn-add:hover {
  background-color: color-mix(in srgb, var(--color-primary) 85%, black);
}
.btn-add:focus-visible {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}

/* ── Icon button (historique) ────────────────────────────────────────────── */
.btn-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 2.25rem;
  height: 2.25rem;
  border-radius: 50%;
  border: 2px solid var(--color-primary);
  background: transparent;
  color: var(--color-primary);
  cursor: pointer;
  transition: background-color 0.15s ease;
}
.btn-icon:hover {
  background-color: color-mix(in srgb, var(--color-primary) 10%, transparent);
}
.btn-icon:focus-visible {
  outline: 2px solid var(--color-primary);
  outline-offset: 2px;
}

/* ── Avatar button ───────────────────────────────────────────────────────── */
.btn-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  border: 2.5px solid var(--color-primary);
  border-radius: 50%;
  background: transparent;
  cursor: pointer;
  transition: box-shadow 0.15s ease;
}
.btn-avatar:hover {
  box-shadow: 0 0 0 3px color-mix(in srgb, var(--color-primary) 25%, transparent);
}
.btn-avatar:focus-visible {
  outline: 2px solid var(--color-primary);
  outline-offset: 3px;
}

.avatar__img {
  width: 2.25rem;
  height: 2.25rem;
  border-radius: 50%;
  object-fit: cover;
  display: block;
}
</style>
