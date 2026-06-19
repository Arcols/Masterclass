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
  completed: boolean
  creator: {
    id: string
    name: string
  }
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

  // Fonction utilitaire interne pour récupérer le token
  const getAuthHeaders = () => {
    const token = localStorage.getItem('token')
    return {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  }

  const fetchEventDetails = async (eventId: string): Promise<EventDetailDTO | null> => {
    isLoading.value = true
    error.value = null

    try {
      const response = await fetch(`http://localhost:8080/api/events/${eventId}`, {
        method: 'GET',
        headers: getAuthHeaders()
      })

      if (!response.ok) throw new Error('Erreur lors de la récupération des détails')

      return await response.json()
    } catch (err: any) {
      error.value = err.message
      return null
    } finally {
      isLoading.value = false
    }
  }

  const addComment = async (eventId: string, userId: string, content: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/events/${eventId}/comments`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({ userId, content })
      })
      if (!response.ok) throw new Error('Erreur lors de l\'ajout du commentaire')
      return await response.json()
    } catch (err: any) {
      console.error(err)
      throw err
    }
  }

  const addNote = async (eventId: string, userId: string, content: string) => {
    try {
      const response = await fetch(`http://localhost:8080/api/events/${eventId}/notes`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({ userId, content })
      })
      if (!response.ok) throw new Error('Erreur lors de l\'ajout de la note')
      return await response.json()
    } catch (err: any) {
      console.error(err)
      throw err
    }
  }

  const toggleEventCompletion = async (eventId: string, userId: string): Promise<boolean | null> => {
    try {
      const response = await fetch(`http://localhost:8080/api/events/${eventId}/toggle-completion`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify({ userId })
      })
      if (!response.ok) throw new Error('Erreur lors du changement de statut')

      return await response.json() // Va renvoyer `true` ou `false`
    } catch (err: any) {
      console.error(err)
      return null
    }
  }

  return {
    isLoading,
    error,
    fetchEventDetails,
    addComment,
    addNote,
    toggleEventCompletion
  }
}
