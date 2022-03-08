import { match_data } from './data';

export const getMatch = (id: number) => {
  return {
    ...match_data,
    id: parseInt(id),
  };
};
