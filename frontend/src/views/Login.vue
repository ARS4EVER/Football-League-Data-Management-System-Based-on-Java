<template>
  <div class="login-container">
    <div class="login-box">
      <h2>登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login" style="width: 100%">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="toRegister" style="width: 100%">没有账号？立即注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    async login() {
      if (!this.form.username || !this.form.password) {
        this.$message.warning('请输入用户名和密码')
        return
      }
      try {
        const res = await this.$http.post('/user/login', null, {
          params: {
            username: this.form.username,
            password: this.form.password
          }
        })
        if (res.data.code === 200) {
          localStorage.setItem('username', this.form.username)
          if (res.data.data) {
            // 确保 role 是数字类型
            const role = res.data.data.role !== null && res.data.data.role !== undefined 
              ? parseInt(res.data.data.role) : 0
            const teamId = res.data.data.teamId !== null && res.data.data.teamId !== undefined 
              ? res.data.data.teamId : null
            localStorage.setItem('userRole', role.toString())
            localStorage.setItem('userTeamId', (teamId || '').toString())
            localStorage.setItem('userInfo', JSON.stringify(res.data.data))
            console.log('登录成功，用户角色:', role, '球队ID:', teamId)
          } else {
            // 如果没有返回用户信息，设置为游客
            localStorage.setItem('userRole', '0')
            localStorage.setItem('userTeamId', '')
          }
          this.$message.success('登录成功')
          // 根据角色跳转到不同页面
          const role = res.data.data.role !== null && res.data.data.role !== undefined 
            ? parseInt(res.data.data.role) : 0
          if (role === 0) {
            // 游客跳转到球队管理
            this.$router.push('/team')
          } else {
            // 其他角色跳转到联赛管理
            this.$router.push('/league')
          }
        } else {
          this.$message.error(res.data.message)
        }
      } catch (error) {
        this.$message.error('登录失败')
      }
    },
    toRegister() {
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('../assets/bg.jpg');
  background-size: cover;
  background-position: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>

