import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

Vue.config.productionTip = false

Vue.use(ElementUI)

// 配置axios
axios.defaults.baseURL = '/api'
axios.defaults.timeout = 10000

// 添加请求拦截器，将用户名添加到请求头
axios.interceptors.request.use(config => {
  const username = localStorage.getItem('username')
  if (username) {
    config.headers.username = username
  }
  return config
}, error => {
  return Promise.reject(error)
})

// 添加响应拦截器，处理权限错误
axios.interceptors.response.use(response => {
  return response
}, error => {
  if (error.response && error.response.data && error.response.data.message) {
    const message = error.response.data.message
    if (message.includes('无权限')) {
      // 权限错误，可能需要更新用户信息
      const username = localStorage.getItem('username')
      if (username) {
        // 重新获取用户信息
        axios.get('/user/current').then(res => {
          if (res.data.code === 200 && res.data.data) {
            localStorage.setItem('userRole', res.data.data.role || 0)
            localStorage.setItem('userTeamId', res.data.data.teamId || '')
          }
        }).catch(() => {})
      }
    }
  }
  return Promise.reject(error)
})

Vue.prototype.$http = axios

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')

