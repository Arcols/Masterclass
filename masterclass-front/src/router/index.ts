import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import testvuecreate from '../views/testvuecreate.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/testcaca',
      name: 'testcaca',
      component: testvuecreate,
    }
  ],
})

export default router
