<script setup lang="ts">
import { RouterLink } from "vue-router";

// ── Props ──────────────────────────────────────────────────────────────────
const props = withDefaults(
  defineProps<{
    /** Affiche les boutons d'action (Ajouter, Historique) - ex: Accueil */
    showActions?: boolean;
    /** Affiche l'avatar du profil - ex: Accueil, Autres pages */
    showProfile?: boolean;
    /** Libellé additionnel sous le titre */
    subtitle?: string;
  }>(),
  {
    showActions: false,
    showProfile: true,
    subtitle: "",
  }
);

// ── Emits ──────────────────────────────────────────────────────────────────
const emit = defineEmits<{
  (e: "add-event"): void;
  (e: "open-history"): void;
  (e: "open-profile"): void;
}>();
</script>

<template>
  <header class="app-header">
    <RouterLink to="/" class="brand">
      <img
        src="@/assets/logo.svg"
        height="45"
        alt="Logo Masterclass"
        class="brand__icon"
      />

      <div class="brand__text">
        <span class="brand__name">Masterclass</span>
        <span class="brand__subtitle" v-if="subtitle">{{ subtitle }}</span>
      </div>
    </RouterLink>

    <div v-if="showActions || showProfile" class="header-actions">

      <template v-if="showActions">
        <button class="btn-add" @click="emit('add-event')" aria-label="Ajouter un événement">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 256 256" fill="currentColor" aria-hidden="true">
            <path d="M228,128a12,12,0,0,1-12,12H140v76a12,12,0,0,1-24,0V140H40a12,12,0,0,1,0-24h76V40a12,12,0,0,1,24,0v76h76A12,12,0,0,1,228,128Z" />
          </svg>
          <span class="btn-add__label">Ajouter un événement</span>
        </button>

        <button class="btn-icon" aria-label="Historique" @click="emit('open-history')">
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 256 256" fill="currentColor" aria-hidden="true">
            <path d="M232,128A104,104,0,1,1,128,24,104.11,104.11,0,0,1,232,128Zm-24,0a80,80,0,1,0-80,80A80.09,80.09,0,0,0,208,128Zm-72,0V80a12,12,0,0,0-24,0v52a12,12,0,0,0,5.37,10l32,21.34a12,12,0,1,0,13.26-20Z" />
          </svg>
        </button>
      </template>

      <button
        v-if="showProfile"
        class="btn-avatar"
        aria-label="Profil"
        @click="emit('open-profile')"
      >
        <img
          src="@/assets/avatar-placeholder.svg"
          alt="Avatar"
          class="avatar__img"
        />
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
  border-radius: var(--radius-small);
  text-decoration: none;
  color: inherit;
  transition: background-color 0.15s ease;
}

.brand:hover {
  background-color: color-mix(
    in srgb,
    var(--color-primary) 12%,
    transparent
  );
}

.brand__text {
  display: flex;
  flex-direction: column;
}

.brand__name {
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 1.2;
}

.brand__subtitle {
  font-size: 0.8125rem;
  color: #6b7280;
}

/* ── Actions ─────────────────────────────────────────────────────────────── */
.header-actions {
  display: flex;
  align-items: center;
  gap: 1.25rem;
}

/* ── Bouton Ajouter ──────────────────────────────────────────────────────── */
.btn-add {
  display: inline-flex;
  align-items: center;
  gap: var(--gap-btn);
  padding: var(--padding-btn);
  border-radius: var(--radius-small);
  border: none;
  background-color: var(--color-primary);
  color: var(--color-background);
  font-size: var(--size-text-btn);
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition-btn);
  white-space: nowrap;
}

.btn-add:hover {
  background-color: color-mix(in srgb, var(--color-primary) 85%, black);
}

/* VERSION COMPACTE */
@media (max-width: 768px) {
  .btn-add {
    width: 2.25rem;
    height: 2.25rem;
    padding: 0;
    border-radius: var(--radius-large);
    justify-content: center;
  }

  .btn-add__label {
    display: none;
  }
}

/* ── Icon button (historique) ────────────────────────────────────────────── */
.btn-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 2.25rem;
  height: 2.25rem;
  border-radius: var(--radius-large);
  border: 2px solid var(--color-primary);
  background: transparent;
  color: var(--color-primary);
  cursor: pointer;
  transition: var(--transition-btn);
}

.btn-icon:hover {
  background-color: color-mix(in srgb, var(--color-primary) 10%, transparent);
}

/* ── Avatar ──────────────────────────────────────────────────────────────── */
.btn-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  border: 2.5px solid var(--color-primary);
  border-radius: var(--radius-large);
  background: transparent;
  cursor: pointer;
}

.avatar__img {
  width: 2.25rem;
  height: 2.25rem;
  border-radius: var(--radius-large);
  object-fit: cover;
}
</style>
