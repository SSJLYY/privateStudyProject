/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const officeRouter = {
  path: '/leave',
  component: Layout,
  meta: {
    title: '在线办公',
    icon: 'nested'
  },
  children: [
    {
      path: 'apply',
      component: () => import('@/views/parent-view'), // Parent router-view
      name: 'apply',
      meta: {title: '请假管理'},
      children: [
        {
          path: 'leave-apply',
          component: () => import('@/views/office/leave/leave-apply'),
          name: 'leave-apply',
          meta: {title: '请假申请'}
        },
        {
          path: 'me-apply',
          component: () => import('@/views/office/leave/me-leave-apply'),
          name: 'me-apply',
          meta: {title: '我的申请'}
        }
      ]
    },
    {
      path: 'task',
      component: () => import('@/views/parent-view'), // Parent router-view
      name: 'task',
      meta: {title: '我的任务'},
      children: [
        {
          path: 'run-task',
          component: () => import('@/views/office/task/run-task-manager'),
          name: 'run-task',
          meta: {title: '代办任务'}
        },
        {
          path: 'his-task',
          component: () => import('@/views/office/task/his-task-manager'),
          name: 'his-task',
          meta: {title: '已办任务'}
        }
      ]
    }
  ]
}

export default officeRouter
