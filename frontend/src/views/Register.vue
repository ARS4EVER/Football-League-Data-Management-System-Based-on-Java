<template>
  <div class="register-container">
    <div class="register-box">
      <h2>注册</h2>
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="游客" :value="0"></el-option>
            <el-option label="赛事组织者" :value="1"></el-option>
            <el-option label="球队工作人员" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属球队" v-if="form.role === 2">
          <el-select v-model="form.teamId" placeholder="请选择球队" style="width: 100%" filterable>
            <el-option v-for="team in teamList" :key="team.id" :label="team.name" :value="team.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" style="width: 100%">注册</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="text" @click="toLogin" style="width: 100%">已有账号？立即登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data() {
    return {
      form: {
        username: '',
        password: '',
        realName: '',
        phone: '',
        role: 0,
        teamId: null
      },
      teamList: []
    }
  },
  mounted() {
    this.loadTeams()
  },
  methods: {
    async loadTeams() {
      try {
        const res = await this.$http.post('/team/query_page', null, {
          params: { current: 1, size: 1000 }
        })
        if (res.data.code === 200) {
          this.teamList = res.data.data.records
        }
      } catch (error) {
        console.error('加载球队列表失败')
      }
    },
    async register() {
      if (!this.form.username || !this.form.password) {
        this.$message.warning('请输入用户名和密码')
        return
      }
      if (this.form.role === 2 && !this.form.teamId) {
        this.$message.warning('球队工作人员必须选择所属球队')
        return
      }
      try {
        const res = await this.$http.post('/user/register', this.form)
        if (res.data.code === 200) {
          this.$message.success('注册成功')
          this.$router.push('/login')
        } else {
          this.$message.error(res.data.message)
        }
      } catch (error) {
        this.$message.error('注册失败')
      }
    },
    toLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('../assets/bg.jpg');
  background-size: cover;
  background-position: center;
}

.register-box {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.register-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>

