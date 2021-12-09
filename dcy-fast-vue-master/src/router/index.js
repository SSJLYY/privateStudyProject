import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'
import officeRouter from "@/router/modules/office";
/* Router Modules */

Vue.use(Router)

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: {title: '首页', icon: 'dashboard', affix: true}
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: {title: '个人信息', icon: 'user', noCache: true}
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  /** when your routing map is too long, you can split it into small modules **/
  {
    path: '/admin',
    component: Layout,
    meta: {
      title: '系统管理',
      icon: 'nested'
    },
    children: [
      {
        path: 'user-manage',
        component: () => import('@/views/admin/user/user-manage'),
        name: 'user-manage',
        meta: {title: '用户管理'}
      },
      {
        path: 'role-manage',
        component: () => import('@/views/admin/role/role-manage'),
        name: 'role-manage',
        meta: {title: '角色管理'}
      },
      {
        path: 'resource-manage',
        component: () => import('@/views/admin/resource/resource-manage'),
        name: 'resource-manage',
        meta: {title: '资源管理'}
      },
      {
        path: 'dept-manage',
        component: () => import('@/views/admin/dept/dept-manage'),
        name: 'dept-manage',
        meta: {title: '部门管理'}
      },
      {
        path: 'post-manage',
        component: () => import('@/views/admin/post/post-manage'),
        name: 'post-manage',
        meta: {title: '岗位管理'}
      },
      {
        path: 'dict-manage',
        component: () => import('@/views/admin/dict/dict-type-manage'),
        name: 'dict-manage',
        meta: {title: '字典管理'}
      },
      {
        path: 'config-manage',
        component: () => import('@/views/admin/config/config-manage'),
        name: 'config-manage',
        meta: {title: '配置管理'}
      },
      {
        path: 'log-manage',
        component: () => import('@/views/admin/log/log-manage'),
        name: 'log-manage',
        meta: {title: '日志管理'}
      },
      {
        path: 'job-manage',
        component: () => import('@/views/admin/job/job-manage'),
        name: 'job-manage',
        meta: {title: '任务管理'}
      },
      {
        path: 'swagger',
        component: Layout,
        children: [
          {
            path: 'http://localhost:8999/doc.html',
            meta: {title: '接口文档'}
          }
        ]
      },
      {
        path: 'druid',
        component: Layout,
        children: [
          {
            path: 'http://localhost:8999/druid/login.html',
            meta: {title: '数据监控'}
          }
        ]
      }
    ]
  },
  {
    path: '/flow',
    component: Layout,
    meta: {
      title: '协同管理',
      icon: 'nested'
    },
    children: [
      {
        path: 'model-manage',
        component: () => import('@/views/flow/model/model-manage'),
        name: 'model-manage',
        meta: {title: '模型管理'}
      },
      {
        path: 'process-manage',
        component: () => import('@/views/flow/process/process-manage'),
        name: 'process-manage',
        meta: {title: '流程管理'}
      },
      {
        path: 'form-manage',
        component: () => import('@/views/flow/form/form-manager.vue'),
        name: 'form-manage',
        meta: {title: '表单管理'}
      }
    ]
  },
  // nestedRouter,
  officeRouter,
  // 404 page must be placed at the end !!!
  {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  mode: 'history',
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
