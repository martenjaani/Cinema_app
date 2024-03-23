import { createRouter, createWebHistory } from 'vue-router'
import Kinokava from '../views/Kinokava.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'kinokava',
      component: Kinokava
    }
  ]
})

export default router
