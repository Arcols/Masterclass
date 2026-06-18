import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import {
  addMonths,
  eachDayOfInterval,
  endOfMonth,
  endOfWeek,
  parseISO,
  startOfMonth,
  startOfWeek,
  subMonths,
  isSameDay,
  isSameMonth,
  isToday,
  format,
} from 'date-fns'

function parseDateOrToday(value: string): Date {
  if (!value) return new Date()
  const parsed = parseISO(value)
  return Number.isNaN(parsed.getTime()) ? new Date() : parsed
}

export function useDatePicker(initialDate = '') {
  const datePickerOpen = ref(false)
  const datePickerRef = ref<HTMLElement | null>(null)
  const calendarMonth = ref<Date>(parseDateOrToday(initialDate))

  const selectedDate = computed(() => parseDateOrToday(initialDate))

  const calendarDays = computed(() => {
    const monthStart = startOfMonth(calendarMonth.value)
    const monthEnd = endOfMonth(calendarMonth.value)
    const gridStart = startOfWeek(monthStart, { weekStartsOn: 1 })
    const gridEnd = endOfWeek(monthEnd, { weekStartsOn: 1 })
    return eachDayOfInterval({ start: gridStart, end: gridEnd })
  })

  const weekdayLabels = ['L', 'M', 'M', 'J', 'V', 'S', 'D']

  function openDatePicker(date = initialDate): void {
    datePickerOpen.value = true
    calendarMonth.value = parseDateOrToday(date)
  }

  function closeDatePicker(): void {
    datePickerOpen.value = false
  }

  function toggleDatePicker(date = initialDate): void {
    if (datePickerOpen.value) {
      closeDatePicker()
      return
    }
    openDatePicker(date)
  }

  function selectDay(day: Date) {
    calendarMonth.value = day
    closeDatePicker()
    return format(day, 'yyyy-MM-dd')
  }

  function previousMonth(): void {
    calendarMonth.value = subMonths(calendarMonth.value, 1)
  }

  function nextMonth(): void {
    calendarMonth.value = addMonths(calendarMonth.value, 1)
  }

  function handleClickOutside(event: MouseEvent): void {
    if (!datePickerOpen.value) return
    const target = event.target as Node | null
    if (target && datePickerRef.value && !datePickerRef.value.contains(target)) {
      closeDatePicker()
    }
  }

  onMounted(() => {
    document.addEventListener('mousedown', handleClickOutside)
  })

  onBeforeUnmount(() => {
    document.removeEventListener('mousedown', handleClickOutside)
  })

  return {
    datePickerOpen,
    datePickerRef,
    calendarMonth,
    selectedDate,
    calendarDays,
    weekdayLabels,
    openDatePicker,
    closeDatePicker,
    toggleDatePicker,
    selectDay,
    previousMonth,
    nextMonth,
    format,
    isSameDay,
    isSameMonth,
    isToday,
  }
}
