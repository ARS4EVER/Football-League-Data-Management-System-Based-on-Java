import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message } from 'element-ui'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/team',
    children: [
      {
        path: '/league',
        name: 'League',
        component: () => import('@/views/League.vue'),
        meta: { requireRole: [1, 2] } // 仅赛事组织者和球队工作人员可访问
      },
      {
        path: '/team',
        name: 'Team',
        component: () => import('@/views/Team.vue'),
        meta: { requireRole: [0, 1, 2] } // 所有角色可访问
      },
      {
        path: '/player',
        name: 'Player',
        component: () => import('@/views/Player.vue'),
        meta: { requireRole: [0, 1, 2] } // 所有角色可访问
      },
      {
        path: '/match',
        name: 'Match',
        component: () => import('@/views/Match.vue'),
        meta: { requireRole: [0, 1, 2] } // 所有角色可访问
      },
      {
        path: '/matchRecord',
        name: 'MatchRecord',
        component: () => import('@/views/MatchRecord.vue'),
        meta: { requireRole: [1, 2] } // 仅赛事组织者和球队工作人员可访问
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫：根据角色权限控制页面访问
router.beforeEach((to, from, next) => {
  // 如果是登录或注册页面，直接放行
  if (to.path === '/login' || to.path === '/register') {
    next()
    return
  }

  // 获取当前用户角色
  const roleStr = localStorage.getItem('userRole')
  const currentRole = roleStr ? parseInt(roleStr) : 0

  // 检查路由是否需要权限
  if (to.meta && to.meta.requireRole) {
    const allowedRoles = to.meta.requireRole
    if (!allowedRoles.includes(currentRole)) {
      // 无权限访问，重定向到有权限的页面
      Message.warning('您无权访问该页面')
      // 根据角色重定向到合适的页面
      if (currentRole === 0) {
        // 游客重定向到球队管理
        next('/team')
      } else {
        // 其他角色重定向到联赛管理
        next('/league')
      }
      return
    }
  }

  // 如果访问根路径，根据角色重定向
  if (to.path === '/') {
    if (currentRole === 0) {
      next('/team') // 游客默认跳转到球队管理
    } else {
      next('/league') // 其他角色默认跳转到联赛管理
    }
    return
  }

  next()
})

export default router

