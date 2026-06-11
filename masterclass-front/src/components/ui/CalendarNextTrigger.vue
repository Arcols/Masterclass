<script setup lang="ts">
import { useCalendar } from '@/composable/useCalendar'
import { addDays, addWeeks, addMonths } from 'date-fns'
import { onKeyStroke } from '@vueuse/core'
import { Button } from '@/components/ui/button'

const { date, setDate, view } = useCalendar()

const next = () => {
  if (view.value === 'day') setDate(addDays(date.value, 1))
  else if (view.value === 'week') setDate(addWeeks(date.value, 1))
  else if (view.value === 'month') setDate(addMonths(date.value, 1))
}

onKeyStroke('ArrowRight', next)
</script>

<template>
  <Button variant="outline" size="icon" @click="next">
    <slot />
  </Button>
</template>
