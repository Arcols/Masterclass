import type { EventPayload } from '@/types/events';

/**
 * Simulates saving an event to the backend.
 * In a real application, this would make an API call (e.g., using fetch or axios).
 * @param payload The event data to save.
 */
export async function createEvent(payload: EventPayload): Promise<EventPayload> {
  console.log('Saving event to the backend:', payload);

  // Simulate a network delay
  await new Promise(resolve => setTimeout(resolve, 500));

  // In a real app, the backend would return the saved object, possibly with an ID.
  const savedEvent = {
    ...payload,
    id: payload.id || Date.now(), // Assign a new ID if it's a new event
  };

  console.log('Event saved successfully:', savedEvent);
  return savedEvent;
}

export async function updateEvent(id: number | string, payload: EventPayload): Promise<EventPayload> {
  console.log(`Updating event with ID ${id} on the backend:`, payload);
  // Simulate a network delay
  await new Promise(resolve => setTimeout(resolve, 500));

  // Return the updated event with the ID
  const updatedEvent = {
    ...payload,
    id: id,
  };

  console.log('Event updated successfully:', updatedEvent);
  return updatedEvent;
}
