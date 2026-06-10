<script setup>
import { ScheduleXCalendar } from '@schedule-x/vue'
import {
  createCalendar,
  createViewDay,
  createViewWeekAgenda,
  createViewMonthAgenda,
  createViewMonthGrid,
  createViewWeek,
} from '@schedule-x/calendar'
import '@schedule-x/theme-default/dist/index.css'
import 'temporal-polyfill/global'

// Do not use a ref here, as the calendar instance is not reactive, and doing so might cause issues
// For updating events, use the events service plugin
const calendarApp = createCalendar({
  selectedDate: Temporal.PlainDate.from('2023-12-19'),
  views: [
    createViewDay(),
    createViewWeekAgenda(),
    createViewWeek(),
    createViewMonthGrid(),
    createViewMonthAgenda(),
  ],
  events: [
    {
      id: 1,
      title: 'Event 1',
      start: Temporal.PlainDate.from('2023-12-19'),
      end: Temporal.PlainDate.from('2023-12-19'),
    },
    {
      id: 2,
      title: 'Event 2',
      start: Temporal.ZonedDateTime.from('2023-12-20T12:00:00+09:00[Asia/Tokyo]'),
      end: Temporal.ZonedDateTime.from('2023-12-20T13:00:00+09:00[Asia/Tokyo]'),
    },
  ],
})
</script>

<template>
  <div>
    <ScheduleXCalendar :calendar-app="calendarApp" />
  </div>
</template>
