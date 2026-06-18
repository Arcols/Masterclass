import { ref, watch } from 'vue'
import type { EventType } from '@/types/event'

// crée un état global (singleton)
const selectedTypes = ref<EventType[]>([])
const selectedGroups = ref<string[]>([])
const showFavoritesOnly = ref(false)

// on lit le sessionStorage au chargement
const initFilters = () => {
  const storedTypes = sessionStorage.getItem('filters_types')
  if (storedTypes) selectedTypes.value = JSON.parse(storedTypes)

  const storedGroups = sessionStorage.getItem('filters_groups')
  if (storedGroups) selectedGroups.value = JSON.parse(storedGroups)

  const storedFav = sessionStorage.getItem('filters_favorites')
  if (storedFav) showFavoritesOnly.value = JSON.parse(storedFav)
}

initFilters()

// on met à jour le sessionStorage à chaque modification
watch(selectedTypes, (newVal) => {
  sessionStorage.setItem('filters_types', JSON.stringify(newVal))
}, { deep: true })

watch(selectedGroups, (newVal) => {
  sessionStorage.setItem('filters_groups', JSON.stringify(newVal))
}, { deep: true })

watch(showFavoritesOnly, (newVal) => {
  sessionStorage.setItem('filters_favorites', JSON.stringify(newVal))
})

// l'export à utiliser dans nos composants
export function useFilters() {
  const resetFilters = () => {
    selectedTypes.value = []
    selectedGroups.value = []
    showFavoritesOnly.value = false
  }

  return {
    selectedTypes,
    selectedGroups,
    showFavoritesOnly,
    resetFilters
  }
}
