<template>
  <div class="match-record-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>比赛记录管理</span>
        <el-button v-if="canEdit" type="primary" @click="handleAdd">新增记录</el-button>
      </div>
      
      <el-form :inline="true" class="search-form">
        <el-form-item label="比赛">
          <el-select v-model="searchForm.matchId" placeholder="请选择比赛" clearable>
            <el-option v-for="match in matchList" :key="match.id" :label="match.matchInfo" :value="match.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" align="center"></el-table-column>
        <el-table-column prop="matchInfo" label="比赛" align="center"></el-table-column>
        <el-table-column prop="playerName" label="球员" align="center"></el-table-column>
        <el-table-column prop="eventType" label="事件类型" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.eventType === 1" type="success">进球</el-tag>
            <el-tag v-else-if="scope.row.eventType === 2" type="warning">黄牌</el-tag>
            <el-tag v-else-if="scope.row.eventType === 3" type="danger">红牌</el-tag>
            <el-tag v-else-if="scope.row.eventType === 4" type="info">助攻</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="eventTime" label="时间(分钟)" align="center"></el-table-column>
        <el-table-column prop="description" label="描述" align="center"></el-table-column>
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
        <el-form-item label="比赛">
          <el-select v-model="form.matchId" placeholder="请选择比赛">
            <el-option v-for="match in matchList" :key="match.id" :label="match.matchInfo" :value="match.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="球员">
          <el-select v-model="form.playerId" placeholder="请选择球员">
            <el-option v-for="player in playerList" :key="player.id" :label="player.name" :value="player.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事件类型">
          <el-select v-model="form.eventType">
            <el-option label="进球" :value="1"></el-option>
            <el-option label="黄牌" :value="2"></el-option>
            <el-option label="红牌" :value="3"></el-option>
            <el-option label="助攻" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间(分钟)">
          <el-input v-model="form.eventTime"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea"></el-input>
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
  name: 'MatchRecord',
  data() {
    return {
      tableData: [],
      matchList: [],
      playerList: [],
      searchForm: {
        matchId: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增记录',
      form: {}
    }
  },
  computed: {
    canEdit() {
      const roleStr = localStorage.getItem('userRole')
      const role = roleStr ? parseInt(roleStr) : 0
      // 只有赛事组织者可以编辑比赛记录
      console.log('MatchRecord页面权限检查 - 角色:', role, '可编辑:', role === 1)
      return role === 1
    }
  },
  mounted() {
    this.loadMatches()
    this.loadPlayers()
    this.queryData()
  },
  methods: {
    async loadMatches() {
      try {
        const res = await this.$http.post('/match/query_page', null, {
          params: { current: 1, size: 1000 }
        })
        if (res.data.code === 200) {
          const matches = res.data.data.records
          matches.forEach(item => {
            item.matchInfo = `ID:${item.id} - ${item.matchDate}`
          })
          this.matchList = matches
        }
      } catch (error) {
        console.error('加载比赛列表失败')
      }
    },
    async loadPlayers() {
      try {
        const res = await this.$http.post('/player/query_page', null, {
          params: { current: 1, size: 1000 }
        })
        if (res.data.code === 200) {
          this.playerList = res.data.data.records
        }
      } catch (error) {
        console.error('加载球员列表失败')
      }
    },
    async queryData() {
      try {
        const res = await this.$http.post('/matchRecord/query_page', null, {
          params: {
            current: this.pagination.current,
            size: this.pagination.size,
            matchId: this.searchForm.matchId
          }
        })
        if (res.data.code === 200) {
          const records = res.data.data.records
          records.forEach(item => {
            const match = this.matchList.find(m => m.id === item.matchId)
            const player = this.playerList.find(p => p.id === item.playerId)
            item.matchInfo = match ? match.matchInfo : ''
            item.playerName = player ? player.name : ''
          })
          this.tableData = records
          this.pagination.total = res.data.data.total
        }
      } catch (error) {
        this.$message.error('查询失败')
      }
    },
    resetSearch() {
      this.searchForm.matchId = null
      this.queryData()
    },
    handleAdd() {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.dialogTitle = '新增记录'
      this.form = {}
      this.dialogVisible = true
    },
    handleEdit(row) {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.dialogTitle = '编辑记录'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        const res = await this.$http.post('/matchRecord/save', this.form)
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
          const res = await this.$http.post('/matchRecord/delete_by_id', null, {
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

