import { createRouter, createWebHistory } from 'vue-router'
import Kinokava from '../views/Kinokava.vue'
import IstmeteValik from '../views/IstmeteValik.vue';
import VaatamisAjalugu from "@/views/VaatamisAjalugu.vue";
import * as path from "path";

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
    },
    {
      path: '/vaatamisajalugu',
      name: 'VaatamisAjalugu',
      component: VaatamisAjalugu

    }
  ]
})

export default router
