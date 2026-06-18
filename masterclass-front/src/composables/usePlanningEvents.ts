import { ref } from 'vue'
import type { EventData } from '@/components/event/EventCard.vue'

export function usePlanningEvents() {
  const events = ref<EventData[]>([])
  const isLoadingEvents = ref(false)

  const getAuthHeaders = () => {
    const token = localStorage.getItem('token')
    return {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  }

  const fetchWeekEvents = async (startDate: string, endDate: string) => {
    isLoadingEvents.value = true
    try {
      const response = await fetch(`http://localhost:8080/api/events/week?startDate=${startDate}&endDate=${endDate}`, {
        method: 'GET',
        headers: getAuthHeaders()
      })

      if (!response.ok) throw new Error("Erreur serveur lors de la récupération du planning")

      const data = await response.json()

      // On mappe le JSON de Spring Boot pour l'interface UI
      events.value = data.map((d: any) => ({
        id: d.eveId,
        type: d.eveType ? d.eveType.toLowerCase() : 'activite',
        title: d.eveTitle,
        date: d.eveDate,
        startTime: d.eveStarthour ? String(d.eveStarthour).substring(0, 5) : '00:00',
        endTime: d.eveEndhour ? String(d.eveEndhour).substring(0, 5) : '01:00',
        description: d.eveDescription,
        location: d.eveLocation,
        group: d.group?.groName,
        subject: d.subject?.subName,

        isCompleted: d.completed,
        isFavorite: false // on verra plus tard comment gérer les favoris
      }))
    } catch (error) {
      console.error("Impossible de charger les événements de la semaine :", error)
      events.value = []
    } finally {
      isLoadingEvents.value = false
    }
  }

  return {
    events,
    isLoadingEvents,
    fetchWeekEvents
  }
}
