<template>
  <div class="match-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>比赛管理</span>
        <el-button v-if="canEdit" type="primary" @click="handleAdd">新增比赛</el-button>
      </div>
      
      <el-form :inline="true" class="search-form">
        <el-form-item label="联赛">
          <el-select v-model="searchForm.leagueId" placeholder="请选择联赛" clearable>
            <el-option v-for="league in leagueList" :key="league.id" :label="league.name" :value="league.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" align="center"></el-table-column>
        <el-table-column prop="leagueName" label="联赛" align="center"></el-table-column>
        <el-table-column prop="homeTeamName" label="主队" align="center"></el-table-column>
        <el-table-column prop="awayTeamName" label="客队" align="center"></el-table-column>
        <el-table-column prop="matchDate" label="比赛时间" align="center"></el-table-column>
        <el-table-column prop="homeScore" label="主队得分" align="center"></el-table-column>
        <el-table-column prop="awayScore" label="客队得分" align="center"></el-table-column>
        <el-table-column prop="stadium" label="比赛场地" align="center"></el-table-column>
        <el-table-column prop="status" label="状态" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button v-if="canEdit" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="canEdit" type="text" style="color: red" @click="handleDelete(scope.row)">删除</el-button>
            <span v-if="!canEdit" style="color: #999">只读</span>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="联赛">
          <el-select v-model="form.leagueId" placeholder="请选择联赛">
            <el-option v-for="league in leagueList" :key="league.id" :label="league.name" :value="league.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="主队">
          <el-select v-model="form.homeTeamId" placeholder="请选择主队">
            <el-option v-for="team in teamList" :key="team.id" :label="team.name" :value="team.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客队">
          <el-select v-model="form.awayTeamId" placeholder="请选择客队">
            <el-option v-for="team in teamList" :key="team.id" :label="team.name" :value="team.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="比赛时间">
          <el-date-picker v-model="form.matchDate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
        <el-form-item label="主队得分">
          <el-input v-model="form.homeScore"></el-input>
        </el-form-item>
        <el-form-item label="客队得分">
          <el-input v-model="form.awayScore"></el-input>
        </el-form-item>
        <el-form-item label="比赛场地">
          <el-input v-model="form.stadium"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="启用" :value="1"></el-option>
            <el-option label="禁用" :value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Match',
  data() {
    return {
      tableData: [],
      leagueList: [],
      teamList: [],
      searchForm: {
        leagueId: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增比赛',
      form: {}
    }
  },
  computed: {
    canEdit() {
      const roleStr = localStorage.getItem('userRole')
      const role = roleStr ? parseInt(roleStr) : 0
      // 只有赛事组织者可以编辑比赛
      console.log('Match页面权限检查 - 角色:', role, '可编辑:', role === 1)
      return role === 1
    }
  },
  mounted() {
    this.loadLeagues()
    this.loadTeams()
    this.queryData()
  },
  methods: {
    async loadLeagues() {
      try {
        const res = await this.$http.post('/league/query_page', null, {
          params: { current: 1, size: 1000 }
        })
        if (res.data.code === 200) {
          this.leagueList = res.data.data.records
        }
      } catch (error) {
        console.error('加载联赛列表失败')
      }
    },
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
    async queryData() {
      try {
        const res = await this.$http.post('/match/query_page', null, {
          params: {
            current: this.pagination.current,
            size: this.pagination.size,
            leagueId: this.searchForm.leagueId
          }
        })
        if (res.data.code === 200) {
          const records = res.data.data.records
          records.forEach(item => {
            const league = this.leagueList.find(l => l.id === item.leagueId)
            const homeTeam = this.teamList.find(t => t.id === item.homeTeamId)
            const awayTeam = this.teamList.find(t => t.id === item.awayTeamId)
            item.leagueName = league ? league.name : ''
            item.homeTeamName = homeTeam ? homeTeam.name : ''
            item.awayTeamName = awayTeam ? awayTeam.name : ''
          })
          this.tableData = records
          this.pagination.total = res.data.data.total
        }
      } catch (error) {
        this.$message.error('查询失败')
      }
    },
    resetSearch() {
      this.searchForm.leagueId = null
      this.queryData()
    },
    handleAdd() {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.dialogTitle = '新增比赛'
      this.form = { status: 1, homeScore: 0, awayScore: 0 }
      this.dialogVisible = true
    },
    handleEdit(row) {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.dialogTitle = '编辑比赛'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        const res = await this.$http.post('/match/save', this.form)
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.queryData()
        }
      } catch (error) {
        this.$message.error('保存失败')
      }
    },
    handleDelete(row) {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.$confirm('确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await this.$http.post('/match/delete_by_id', null, {
            params: { id: row.id }
          })
          if (res.data.code === 200) {
            this.$message.success('删除成功')
            this.queryData()
          } else {
            this.$message.error(res.data.message || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.queryData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.queryData()
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>

