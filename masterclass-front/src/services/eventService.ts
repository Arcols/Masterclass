import type { EventPayload } from '@/types/events'
import type { EventData } from '@/components/event/EventCard.vue'

const API_BASE_EVENTS = 'http://localhost:8080/api/events'

function getAuthHeaders(): Record<string, string> {
  const token = localStorage.getItem('token')
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  }
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }
  return headers
}

export async function createEvent(payload: EventPayload): Promise<void> {
  const res = await fetch(API_BASE_EVENTS, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({
      type: payload.type.toUpperCase(), // "devoir" → "DEVOIR"
      title: payload.title,
      date: payload.date,
      startTime: payload.startTime,
      endTime: payload.endTime,
      description: payload.description ?? null,
      location: payload.location ?? null,
      submissionLink: payload.submissionLink ?? null,
      subjectId: payload.subject ?? null, // ⬅️ subject → subjectId
      groupId: payload.group ?? null, // ⬅️ group  → groupId
    }),
  })

  if (!res.ok) {
    const errorText = await res.text()
    throw new Error(errorText || `Erreur HTTP: ${res.status}`)
  }
}

export async function updateEvent(
  id: number | string,
  payload: EventPayload,
): Promise<EventPayload> {
  // TODO: brancher le vrai endpoint quand il sera prêt
  await new Promise((resolve) => setTimeout(resolve, 500))
  return { ...payload, id }
}
