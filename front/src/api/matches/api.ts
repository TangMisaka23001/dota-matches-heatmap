import { defHttp } from '/@/utils/http/axios';

export const getMatch = (id: number) => {
  return defHttp.get({
    url: `/match/${id}`,
  });
};
