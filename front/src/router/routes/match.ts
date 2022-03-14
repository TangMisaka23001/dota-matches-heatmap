import type { AppRouteModule } from '/@/router/types';

export const matchesRoutes: AppRouteModule[] = [
  {
    path: '/matches/index',
    name: 'matches',
    component: () => import('/@/views/match/index.vue'),
    meta: {
      title: 'matches',
      ignoreAuth: true,
    },
  },
];

export const staticRouteNames = matchesRoutes.map((item) => item.name);
