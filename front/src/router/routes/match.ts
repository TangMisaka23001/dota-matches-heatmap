import type { AppRouteModule } from '/@/router/types';

export const matchesRoutes: AppRouteModule[] = [
  {
    path: '/matches-heatmap',
    name: 'matches-heatmap',
    component: () => import('/@/views/match/index.vue'),
    meta: {
      title: 'matches-heatmap',
      ignoreAuth: true,
    },
  },
];

export const staticRouteNames = matchesRoutes.map((item) => item.name);
