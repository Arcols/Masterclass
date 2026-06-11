import { inject, type Ref } from 'vue'
import type { Locale } from 'date-fns'

export type View = 'day' | 'week' | 'month'

export type CalendarEvent = {
  id: string
  start: Date
  end: Date
  title: string
  color?: string
  type?: 'homework' | 'event'
  // ... autres propriétés
}

export type CalendarContextState = {
  view: Ref<View>
  date: Ref<Date>
  events: Ref<CalendarEvent[]>
  locale: Locale
  today: Date
  setView: (v: View) => void
  setDate: (d: Date) => void
  onTimeSlotClick?: (date: Date) => void
}

export const CalendarInjectionKey = Symbol('CalendarContext')

export function useCalendar(): CalendarContextState {
  const context = inject<CalendarContextState>(CalendarInjectionKey)
  if (!context) {
    throw new Error("useCalendar doit être utilisé à l'intérieur de <Calendar>")
  }
  return context
}
