import type { EventData } from '@/components/event/EventCard.vue'

const API_BASE_EVENTS = `${import.meta.env.VITE_API_BASE_URL}/api/event`

// Forme brute renvoyée par le back
interface SubjectResponse {
  subId: string
  subName: string
}

interface GroupResponse {
  groId: string
  groName: string
}

interface EventSummaryResponse {
  eveId: string
  eveType: 'DEVOIR' | 'EXAMEN' | 'ACTIVITE' | 'SPORT'
  eveTitle: string
  eveDate: string
  eveStarthour: string
  eveEndhour: string
  eveDescription: string | null
  eveLocation: string | null
  eveSubmissionLink: string | null
  subject: SubjectResponse | null
  group: GroupResponse | null
}

const TYPE_MAP: Record<EventSummaryResponse['eveType'], EventData['type']> = {
  DEVOIR: 'devoir',
  EXAMEN: 'examen',
  ACTIVITE: 'activite',
  SPORT: 'sport',
}

function mapToEventData(e: EventSummaryResponse): EventData {
  return {
    id: e.eveId,
    type: TYPE_MAP[e.eveType],
    title: e.eveTitle,
    subject: e.subject?.subName ?? null,
    description: e.eveDescription,
    date: e.eveDate,
    startTime: e.eveStarthour,
    endTime: e.eveEndhour,
    submissionLink: e.eveSubmissionLink ?? undefined,
    location: e.eveLocation ?? undefined,
    group: e.group?.groId ?? '',
    isCompleted: false, // pas encore géré par le back, voir note plus bas
    isFavorite: false, // idem
  }
}

export async function getTodoList(): Promise<EventData[]> {
  const token = localStorage.getItem('token')

  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  }

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  const res = await fetch(`${API_BASE_EVENTS}/todolist`, {
    method: 'GET',
    headers,
  })

  if (!res.ok) throw new Error(`Erreur HTTP: ${res.status}`)

  const data: EventSummaryResponse[] = await res.json()
  return data.map(mapToEventData)
}
