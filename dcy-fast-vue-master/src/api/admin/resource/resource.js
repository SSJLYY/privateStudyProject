import axios from '_l/api.request'

/**
 * 添加模块
 * @param module
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addResource = (module) => {
  return axios.request({
    url: '/system/resource/save',
    method: 'post',
    data: module
  })
}

/**
 * 修改模块
 * @param module
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateResource = (module) => {
  return axios.request({
    url: '/system/resource/update',
    method: 'post',
    data: module
  })
}

/**
 * 删除模块
 * @param moduleId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteResourceById = (moduleId) => {
  return axios.request({
    url: '/system/resource/delete',
    method: 'post',
    params: {
      id: moduleId
    }
  })
}

/**
 * 获取模块tree-table数据
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getResourceTreeList = () => {
  return axios.request({
    url: '/system/resource/getResourceTreeList'
  })
}

/**
 * 获取菜单列表
 * @returns {AxiosPromise}
 */
export const getMenuList = () => {
  return axios.request({
    url: '/system/resource/getMenuList'
  })
}

/**
 * 获取tree列表数据
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getResourceIdListByRoleId = (roleId) => {
  return axios.request({
    url: '/system/resource/getResourceIdListByRoleId',
    params: {
      roleId: roleId
    }
  })
}

/**
 * 根据登录人获取目录和菜单路由
 * @returns {AxiosPromise}
 */
export const getRouterList = () => {
  return axios.request({
    url: '/system/resource/getRouterList'
  })
}
