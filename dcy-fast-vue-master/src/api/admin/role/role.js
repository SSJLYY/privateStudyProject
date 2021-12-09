import axios from '_l/api.request'

/**
 * 添加角色
 * @param role
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addRole = (role) => {
  return axios.request({
    url: '/system/role/save',
    method: 'post',
    data: role
  })
}

/**
 * 修改角色
 * @param role
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateRole = (role) => {
  return axios.request({
    url: '/system/role/update',
    method: 'post',
    data: role
  })
}

/**
 * 删除角色
 * @param roleId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteRoleById = (roleId) => {
  return axios.request({
    url: '/system/role/delete',
    method: 'post',
    params: {
      id: roleId
    }
  })
}

/**
 * 批量删除角色
 * @param roleIds
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchRoleById = (roleIds) => {
  return axios.request({
    url: '/system/role/deleteBatch',
    method: 'post',
    data: roleIds
  })
}

/**
 * 获取角色列表
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getRoleAllList = () => {
  return axios.request({
    url: '/system/role/all'
  })
}

/**
 * 根据角色id查询已授权的权限列表
 * @param roleId
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getAuthPowerListByRoleId = (roleId) => {
  return axios.request({
    url: '/system/role/getAuthPowerListByRoleId',
    params: {
      roleId: roleId
    }
  })
}


/**
 * 保存授权权限
 * @param roleResourceDto
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const saveAuthResource = (roleResourceDto) => {
  return axios.request({
    url: '/system/role/saveAuthResource',
    method: 'post',
    data: roleResourceDto
  })
};

/**
 * 获取已授权的数据范围部门ids
 * @param roleId
 * @returns {AxiosPromise}
 */
export const getDataScopeDeptIdsByRoleId = (roleId) => {
  return axios.request({
    url: '/system/role/getDataScopeDeptIdsByRoleId',
    params: {
      roleId: roleId
    }
  })
}
