<template>
  <div class="team-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>球队管理</span>
        <el-button v-if="canEdit" type="primary" @click="handleAdd">新增球队</el-button>
      </div>
      
      <el-form :inline="true" class="search-form">
        <el-form-item label="球队名称">
          <el-input v-model="searchForm.name" placeholder="请输入球队名称" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" align="center"></el-table-column>
        <el-table-column prop="name" label="球队名称" align="center"></el-table-column>
        <el-table-column prop="city" label="所在城市" align="center"></el-table-column>
        <el-table-column prop="coach" label="主教练" align="center"></el-table-column>
        <el-table-column prop="homeStadium" label="主场" align="center"></el-table-column>
        <el-table-column prop="foundedYear" label="成立年份" align="center"></el-table-column>
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
        <el-form-item label="球队名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="所在城市">
          <el-input v-model="form.city"></el-input>
        </el-form-item>
        <el-form-item label="主教练">
          <el-input v-model="form.coach"></el-input>
        </el-form-item>
        <el-form-item label="主场">
          <el-input v-model="form.homeStadium"></el-input>
        </el-form-item>
        <el-form-item label="成立年份">
          <el-input v-model="form.foundedYear"></el-input>
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
  name: 'Team',
  data() {
    return {
      tableData: [],
      searchForm: {
        name: ''
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增球队',
      form: {}
    }
  },
  computed: {
    canEdit() {
      const roleStr = localStorage.getItem('userRole')
      const role = roleStr ? parseInt(roleStr) : 0
      // 只有赛事组织者可以编辑球队
      console.log('Team页面权限检查 - 角色:', role, '可编辑:', role === 1)
      return role === 1
    }
  },
  mounted() {
    this.queryData()
  },
  methods: {
    async queryData() {
      try {
        const res = await this.$http.post('/team/query_page', null, {
          params: {
            current: this.pagination.current,
            size: this.pagination.size,
            name: this.searchForm.name
          }
        })
        if (res.data.code === 200) {
          this.tableData = res.data.data.records
          this.pagination.total = res.data.data.total
        }
      } catch (error) {
        this.$message.error('查询失败')
      }
    },
    resetSearch() {
      this.searchForm.name = ''
      this.queryData()
    },
    handleAdd() {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.dialogTitle = '新增球队'
      this.form = { status: 1 }
      this.dialogVisible = true
    },
    handleEdit(row) {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.dialogTitle = '编辑球队'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        const res = await this.$http.post('/team/save', this.form)
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
          const res = await this.$http.post('/team/delete_by_id', null, {
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

