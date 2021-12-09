package com.dcy.workflow.listener;
/**
 * UserTask 使用的任务监听器
 *  注意事项：
 *  1.第一个节点：申请节点需要指定人员，使用表达式方式：${userId}
 *  2.所有的审批节点都需要添加两个任务监听器
 *      事件 create: 类： com.dcy.workflow.listener.TaskCandidateListener（用于修改每个节点的审批人）
 *      事件 complete: 类： com.dcy.workflow.listener.RejectTaskListener（用于修改业务驳回状态）
 *  3.最后一个完成节点需要添加一个任务监听器
 *      事件 complete: 类： com.dcy.workflow.listener.CompleteTaskListener（用于修改业务完成状态）
 *
 * create（创建）：当任务已经创建，并且所有任务参数都已经设置时触发。
 * assignment（指派）：当任务已经指派给某人时触发。请注意：当流程执行到达用户任务时，在触发create事件之前，会首先触发assignment事件。
 * complete（完成）：当任务已经完成，从运行时数据中删除前触发。
 * delete（删除）：在任务即将被删除前触发。请注意任务由completeTask正常完成时也会触发。
 */