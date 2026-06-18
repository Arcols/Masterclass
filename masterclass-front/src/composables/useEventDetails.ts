import { ref } from 'vue'

// DTO du backend Spring Boot
export interface EventDetailDTO {
  id: string
  type: string
  title: string
  date: string
  startTime: string
  endTime: string
  description: string
  location: string
  submissionLink: string
  subjectName: string
  groupName: string
  comments: Array<{
    id: string
    content: string
    date: string
    authorName: string
  }>
  personalNotes: Array<{
    id: string
    content: string
    date: string
  }>
}

export function useEventDetails() {
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  const fetchEventDetails = async (eventId: string): Promise<EventDetailDTO | null> => {
    isLoading.value = true
    error.value = null

    try {
      // Récupération du token JWT (à adapter selon ta gestion d'auth)
      const token = localStorage.getItem('token')

      const response = await fetch(`http://localhost:8080/api/events/${eventId}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      })

      if (!response.ok) {
        throw new Error('Erreur lors de la récupération des détails')
      }

      const data = await response.json()
      return data

    } catch (err: any) {
      error.value = err.message
      return null
    } finally {
      isLoading.value = false
    }
  }

  return {
    isLoading,
    error,
    fetchEventDetails
  }
}
