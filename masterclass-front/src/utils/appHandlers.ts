import type { Ref } from 'vue'
import type { EventPayload } from '@/types/events'
import type { EventData } from '@/components/EventCard.vue'

export function makeOnAddEvent(addEventRef: Ref<InstanceType<any> | null>) {
  return function onAddEvent(): void {
    addEventRef.value?.addEventPopup()
  }
}

export function makeOnRequestAdd(addEventRef: Ref<InstanceType<any> | null>) {
  return function onRequestAdd(payload: { date: string; startTime: string }): void {
    const [hStr, mStr] = payload.startTime.split(':')
    const h = Number(hStr)
    const m = Number(mStr)
    let endH = h + 1
    let endM = m
    if (endH >= 24) { endH = 23; endM = 59 }

    const payloadEvent: EventPayload = {
      type: 'devoir',
      title: '',
      description: '',
      subject: undefined,
      location: '',
      date: payload.date,
      startTime: `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`,
      endTime: `${String(endH).padStart(2, '0')}:${String(endM).padStart(2, '0')}`,
    }

    addEventRef.value?.addEventPopup(payloadEvent)
  }
}

export function makeHandleUpdateStatus(selectedEventRef: Ref<EventData | null>) {
  return function handleUpdateStatus(id: string, newValue: boolean): void {
    if (selectedEventRef.value && selectedEventRef.value.id === id) {
      selectedEventRef.value.isCompleted = newValue
    }
  }
}

export function makeHandleDelete(selectedEventRef: Ref<EventData | null>) {
  return function handleDelete(id: string): void {
    console.log('Supprimer l\'event', id)
    selectedEventRef.value = null
  }
}

export function makeHandleEdit(addEventRef: Ref<InstanceType<any> | null>) {
  return function handleEdit(event: EventData): void {
    const payload: EventPayload = {
      id: event.id,
      type: event.type,
      title: event.title,
      description: event.description ?? '',
      subject: event.subject ?? undefined,
      location: event.location ?? '',
      date: event.date,
      startTime: event.startTime,
      endTime: event.endTime,
    }

    addEventRef.value?.addEventPopup(payload)
  }
}

