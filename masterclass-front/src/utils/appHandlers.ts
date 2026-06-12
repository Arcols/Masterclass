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
    const [hStr = '0', mStr = '0'] = payload.startTime.split(':')
    const h = Number(hStr)
    const m = Number(mStr)
    const startMinutes = Math.min(Math.max(h * 60 + m, 0), 23 * 60 + 30)
    const startH = Math.floor(startMinutes / 60)
    const startM = startMinutes % 60
    const endMinutes = Math.min(startMinutes + 60, 23 * 60 + 30)
    const endH = Math.floor(endMinutes / 60)
    const endM = endMinutes % 60

    const payloadEvent: EventPayload = {
      type: 'devoir',
      title: '',
      description: '',
      subject: undefined,
      location: '',
      date: payload.date,
      startTime: `${String(startH).padStart(2, '0')}:${String(startM).padStart(2, '0')}`,
      endTime: `${String(endH).padStart(2, '0')}:${String(endM).padStart(2, '0')}`,
    }

    addEventRef.value?.addEventPopup(payloadEvent)
  }
}

export function makeHandleUpdateStatus(selectedEventRef: Ref<EventData | null>) {
  return function handleUpdateStatus(id: string, newValue: boolean): void {
    if (selectedEventRef.value?.id === id) {
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

