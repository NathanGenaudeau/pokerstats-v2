import { createRouter, createWebHistory } from 'vue-router';
import Upload from '../components/Upload.vue';
import Home from '../components/Home.vue';
import Range from '../components/Range.vue';
import Tournament from '../components/Tournament.vue';
import Player from '../components/Player.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/upload',
      name: 'upload',
      component: Upload,
    },
    {
      path: '/range',
      name: 'range',
      component: Range,
    },
    {
      path: '/tournament',
      name: 'tournament',
      component: Tournament,
    },
    {
      path: '/player',
      name: 'player',
      component: Player,
    },
    {
      path: '/:pathMatch(.*)*',
      component: Home,
    },
  ],
});

export default router;