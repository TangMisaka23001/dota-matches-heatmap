import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';

const matches: AppRouteModule = {
  path: '/matches',
  name: 'matches',
  component: LAYOUT,
  redirect: '/matches/index',
  meta: {
    hideChildrenInMenu: true,
    icon: 'simple-icons:about-dot-me',
    title: 'matches',
    orderNo: 1,
  },
  children: [
    {
      path: 'index',
      name: 'matches-index',
      component: () => import('/@/views/match/index.vue'),
      meta: {
        title: 'matches-index',
        hideMenu: true,
      },
    },
  ],
};

export default matches;
