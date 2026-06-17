// This file centralizes the event type definitions.

export type EventType = 'devoir' | 'activite' | 'sport' | 'examen';

export interface EventPayload {
  id?: number | string; // Optional: will be present for existing events
  type: EventType;
  title: string;
  description: string;
  group?: string;
  subject?: string;
  location: string;
  date: string; // Format: YYYY-MM-DD
  startTime: string; // Format: HH:mm
  endTime: string; // Format: HH:mm
  dueTime: string; // Format: HH:mm, optional for 'devoir' and 'examen'
  submissionLink?: string; // Optional: for 'devoir' type
}

