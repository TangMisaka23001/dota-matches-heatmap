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
      name: 'matches',
      component: () => import('/@/views/match/index.vue'),
      meta: {
        title: 'matches',
        icon: 'simple-icons:about-dot-me',
        hideMenu: true,
      },
    },
  ],
};

export default matches;
