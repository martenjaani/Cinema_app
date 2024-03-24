import { createRouter, createWebHistory } from 'vue-router'
import Kinokava from '../views/Kinokava.vue'
import IstmeteValik from '../views/IstmeteValik.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'kinokava',
      component: Kinokava
    },
    {
      path: '/istmetevalik/:id',
      name: 'IstmeteValik',
      component: IstmeteValik,
      props: true
    }
  ]
})

export default router
