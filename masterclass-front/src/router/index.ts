import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
<<<<<<< HEAD
import DetailEvent from '../components/DetailEvent.vue'
=======
>>>>>>> 628e67b80458adafeb3dc08071bcc0ede9da9e05
import TestDetailEvent from '@/views/TestDetailEvent.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/detail-event',
      name: 'DetailEventModal',
      component: TestDetailEvent,
    }
  ],
})

export default router
