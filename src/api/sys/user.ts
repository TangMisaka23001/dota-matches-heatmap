import { defHttp } from '/@/utils/http/axios';

enum Api {
  Login = '/login',
  Logout = '/logout',
  GetUserInfo = '/getUserInfo',
  GetPermCode = '/getPermCode',
}

/**
 * @description: user login api
 */
export function loginApi() {
  return Promise.resolve({
    userId: '123',
    token: 'token',
    roles: [{ roleName: 'super', value: 'super' }],
  });
}

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  return Promise.resolve({
    userId: '123',
    username: 'username',
    realName: 'realName',
    roles: [{ roleName: 'super', value: 'super' }],
    avatar: '',
  });
}

export function getPermCode() {
  return defHttp.get<string[]>({ url: Api.GetPermCode });
}

export function doLogout() {
  return defHttp.get({ url: Api.Logout });
}
