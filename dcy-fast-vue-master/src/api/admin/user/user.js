import axios from '_l/api.request'

/**
 * 登录
 * @param data
 * @returns {AxiosPromise}
 */
export const login = (data) => {
  return axios.request({
    url: '/login',
    method: 'post',
    data
  })
}

/**
 * 获取用户信息
 * @returns {AxiosPromise}
 */
export const getInfo = () => {
  return axios.request({
    url: '/getUserInfo',
    method: 'get'
  })
}

/**
 * 退出
 * @returns {AxiosPromise}
 */
export const logout = () => {
  return axios.request({
    url: '/logout',
    method: 'post'
  })
}
/**
 * 添加用户
 * @param userInfo
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addUserInfo = (userInfo) => {
  return axios.request({
    url: '/system/user/save',
    method: 'post',
    data: userInfo
  })
}

/**
 * 修改用户
 * @param userInfo
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateUserInfo = (userInfo) => {
  return axios.request({
    url: '/system/user/update',
    method: 'post',
    data: userInfo
  })
}

/**
 * 修改用户基本信息
 * @param userInfo
 * @returns {AxiosPromise}
 */
export const updateInfo = (userInfo) => {
  return axios.request({
    url: '/system/user/updateInfo',
    method: 'post',
    data: userInfo
  })
}

/**
 * 删除用户
 * @param userInfoId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteUserInfoById = (userInfoId) => {
  return axios.request({
    url: '/system/user/delete',
    method: 'post',
    params: {
      id: userInfoId
    }
  })
}

/**
 * 批量删除用户
 * @param userInfoIds
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchUserInfoById = (userInfoIds) => {
  return axios.request({
    url: '/system/user/deleteBatch',
    method: 'post',
    data: userInfoIds
  })
};

/**
 * 重置密码
 * @param userInfo
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const resetPassword = (userInfo) => {
  return axios.request({
    url: '/system/user/resetPassword',
    method: 'post',
    data: userInfo
  })
};

/**
 * 根据用户id查询已授权的角色列表
 * @param userId
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getAuthRoleListByUserId = (userId) => {
  return axios.request({
    url: '/system/user/getAuthRoleListByUserId',
    params: {
      userId: userId
    }
  })
};

/**
 * 保存授权角色
 * @param userInfoRoleDTO
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const saveAuthRole = (userInfoRoleDTO) => {
  return axios.request({
    url: '/system/user/saveAuthRole',
    method: 'post',
    data: userInfoRoleDTO
  })
};

/**
 * 根据用户id查询已授权的用户组列表
 * @param userId
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getAuthGroupListByUserId = (userId) => {
  return axios.request({
    url: '/system/user/getAuthGroupListByUserId',
    params: {
      userId: userId
    }
  })
};

/**
 * 保存授权用户组
 * @param userInfoGroupDTO
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const saveAuthGroup = (userInfoGroupDTO) => {
  return axios.request({
    url: '/system/user/saveAuthGroup',
    method: 'post',
    data: userInfoGroupDTO
  })
};

/**
 * 根据用户名获取用户信息
 * @param username 用户名
 * @returns {*|AxiosPromise<any>|ClientRequest|void|ClientHttp2Stream}
 */
export const getUserInfoByUsername = (username) => {
  return axios.request({
    url: '/system/user/getUserInfoByUsername',
    params: {
      username: username
    }
  })
};

/**
 * 根据用户id查询岗位列表
 * @param userId
 * @returns {*}
 */
export const getPostListByUserId = (userId) => {
  return axios.request({
    url: '/system/user/getPostListByUserId',
    params: {
      userId: userId
    }
  })
};

/**
 * 修改密码
 * @param userInfo
 * @returns {*}
 */
export const updatePass = (userInfo) => {
  return axios.request({
    url: '/system/user/updatePass',
    method: 'post',
    data: userInfo
  })
}

export const getOptionDataByFlowType = (flowType, search) => {
  return axios.request({
    url: '/system/user/getOptionDataByFlowType',
    params: {
      flowType: flowType,
      search: search
    }
  })
};
