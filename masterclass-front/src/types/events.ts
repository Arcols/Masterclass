// This file centralizes the event type definitions.

export type EventType = 'devoir' | 'activite' | 'sport' | 'examen';

export interface CreateEventPayload {
  type: EventData['type']
  title: string
  date: string // format ISO : "2026-06-20"
  startTime: string // format : "08:00:00"
  endTime: string // format : "09:00:00"
  description?: string
  location?: string
  submissionLink?: string
  subjectId?: string
  groupId: string
}

