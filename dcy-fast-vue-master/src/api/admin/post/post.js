import axios from '_l/api.request'

/**
 * 添加岗位表
 * @param post
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addPost = (post) => {
  return axios.request({
    url: '/system/post/save',
    method: 'post',
    data: post
  })
}

/**
 * 修改岗位表
 * @param post
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updatePost = (post) => {
  return axios.request({
    url: '/system/post/update',
    method: 'post',
    data: post
  })
}

/**
 * 删除岗位表
 * @param postId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deletePostById = (postId) => {
  return axios.request({
    url: '/system/post/delete',
    method: 'post',
    params: {
      id: postId
    }
  })
}

/**
 * 批量删除岗位表
 * @param postIds
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchPostById = (postIds) => {
  return axios.request({
    url: '/system/post/deleteBatch',
    method: 'post',
    data: postIds
  })
}

/**
 * 获取所有数据
 * @returns {AxiosPromise}
 */
export const getPostListAll = () => {
  return axios.request({
    url: '/system/post/all',
    method: 'get'
  })
}
