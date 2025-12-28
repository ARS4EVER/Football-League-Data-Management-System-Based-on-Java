<template>
  <div class="main-layout">
    <el-container>
      <el-header>
        <div class="header-content">
          <h2>足球联赛数据管理系统</h2>
          <div class="user-info">
            <el-button v-if="!username || username === '用户'" type="text" style="color: white;" @click="toLogin">登录</el-button>
            <el-button v-if="!username || username === '用户'" type="text" style="color: white;" @click="toRegister">注册</el-button>
            <span v-if="username && username !== '用户'">欢迎，{{ username }} ({{ roleName }})</span>
            <el-button v-if="username && username !== '用户'" type="text" style="color: white;" @click="logout">退出</el-button>
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside-menu">
          <el-menu
            :default-active="activeMenu"
            router
            background-color="#f5f5f5"
            text-color="#333"
            active-text-color="#409EFF"
            class="sidebar-menu">
            <!-- 联赛管理：仅赛事组织者和球队工作人员可见 -->
            <el-menu-item v-if="canShowLeague" index="/league">
              <i class="el-icon-trophy"></i>
              <span>联赛管理</span>
            </el-menu-item>
            <!-- 球队管理：所有角色可见 -->
            <el-menu-item index="/team">
              <i class="el-icon-medal"></i>
              <span>球队管理</span>
            </el-menu-item>
            <!-- 球员管理：所有角色可见 -->
            <el-menu-item index="/player">
              <i class="el-icon-user"></i>
              <span>球员管理</span>
            </el-menu-item>
            <!-- 比赛管理：所有角色可见 -->
            <el-menu-item index="/match">
              <i class="el-icon-video-camera"></i>
              <span>比赛管理</span>
            </el-menu-item>
            <!-- 比赛记录：仅赛事组织者和球队工作人员可见 -->
            <el-menu-item v-if="canShowMatchRecord" index="/matchRecord">
              <i class="el-icon-edit"></i>
              <span>比赛记录</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'MainLayout',
  data() {
    return {
      username: localStorage.getItem('username') || '用户'
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    roleName() {
      const roleStr = localStorage.getItem('userRole')
      const role = roleStr ? parseInt(roleStr) : 0
      const roleMap = {
        0: '游客',
        1: '赛事组织者',
        2: '球队工作人员'
      }
      return roleMap[role] || '游客'
    },
    currentRole() {
      const roleStr = localStorage.getItem('userRole')
      return roleStr ? parseInt(roleStr) : 0
    },
    // 游客只能查看球队、球员、比赛信息，不能查看联赛管理和比赛记录
    canShowLeague() {
      // 仅赛事组织者和球队工作人员可见
      return this.currentRole === 1 || this.currentRole === 2
    },
    canShowMatchRecord() {
      // 仅赛事组织者和球队工作人员可见
      return this.currentRole === 1 || this.currentRole === 2
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    async loadUserInfo() {
      const username = localStorage.getItem('username')
      if (username) {
        try {
          const res = await this.$http.get('/user/current')
          if (res.data.code === 200 && res.data.data) {
            const user = res.data.data
            // 确保 role 是数字类型
            const role = user.role !== null && user.role !== undefined ? parseInt(user.role) : 0
            localStorage.setItem('userRole', role.toString())
            localStorage.setItem('userTeamId', (user.teamId || '').toString())
            localStorage.setItem('userInfo', JSON.stringify(user))
            this.username = username
          }
        } catch (error) {
          console.error('获取用户信息失败', error)
        }
      }
    },
    toLogin() {
      this.$router.push('/login')
    },
    toRegister() {
      this.$router.push('/register')
    },
    logout() {
      localStorage.removeItem('username')
      localStorage.removeItem('userRole')
      localStorage.removeItem('userTeamId')
      localStorage.removeItem('userInfo')
      this.username = '用户'
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.main-layout {
  height: 100vh;
}

.el-header {
  background-color: #409EFF;
  color: white;
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.header-content h2 {
  margin: 0;
  font-size: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.el-aside {
  background-color: #f5f5f5;
}

.aside-menu {
  height: calc(100vh - 60px);
}

.sidebar-menu {
  height: 100%;
  border-right: 1px solid #e6e6e6;
}

.el-main {
  background-color: #fff;
  padding: 20px;
  height: calc(100vh - 60px);
  overflow-y: auto;
}
</style>

