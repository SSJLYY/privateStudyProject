import axios from '@/libs/api.request'

/**
 * 完成任务并且添加批注信息+通过状态+批注状态+流程变量
 * @param taskId
 * @param comment
 * @param adopt
 * @returns {*}
 */
export const completeTaskAndCommentAndSetVar = (taskId, comment, adopt) => {
  return axios.request({
    url: '/flow/task/completeTaskAndCommentAndSetVar',
    method: 'post',
    params: {
      taskId: taskId,
      comment: comment,
      adopt: adopt
    }
  })
}

/**
 * 完成任务并且添加批注信息+通过状态+批注状态
 * @param taskId
 * @param comment
 * @param adopt
 * @returns {*}
 */
export const completeTaskAndCommentAndAdopt = (taskId, comment, adopt) => {
  return axios.request({
    url: '/flow/task/completeTaskAndCommentAndAdopt',
    method: 'post',
    params: {
      taskId: taskId,
      comment: comment,
      adopt: adopt
    }
  })
}

/**
 * 完成任务并且添加批注信息+通过状态
 * @param taskId
 * @param comment
 * @returns {*}
 */
export const completeTaskAndComment = (taskId, comment) => {
  return axios.request({
    url: '/flow/task/completeTaskAndComment',
    method: 'post',
    params: {
      taskId: taskId,
      comment: comment
    }
  })
}
