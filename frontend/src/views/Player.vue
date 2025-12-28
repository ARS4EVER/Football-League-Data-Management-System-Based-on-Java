<template>
  <div class="player-container">
    <el-card>
      <div slot="header" class="card-header">
        <span>球员管理</span>
        <el-button v-if="canEdit" type="primary" @click="handleAdd">新增球员</el-button>
      </div>

      <el-form :inline="true" class="search-form">
        <el-form-item label="球员姓名">
          <el-input v-model="searchForm.name" placeholder="请输入球员姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="球队">
          <el-select v-model="searchForm.teamId" placeholder="请选择球队" clearable>
            <el-option v-for="team in teamList" :key="team.id" :label="team.name" :value="team.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border>
        <el-table-column prop="id" label="ID" align="center"></el-table-column>
        <el-table-column prop="name" label="球员姓名" align="center"></el-table-column>
        <el-table-column prop="number" label="球衣号码" align="center"></el-table-column>
        <el-table-column prop="position" label="位置" align="center"></el-table-column>
        <el-table-column prop="age" label="年龄" align="center"></el-table-column>
        <el-table-column prop="nationality" label="国籍" align="center"></el-table-column>
        <el-table-column prop="height" label="身高(米)" align="center"></el-table-column>
        <el-table-column prop="weight" label="体重(公斤)" align="center"></el-table-column>
        <el-table-column prop="dominantFoot" label="惯用脚" align="center"></el-table-column>
        <el-table-column prop="goals" label="进球数" align="center"></el-table-column>
        <el-table-column prop="assists" label="助攻数" align="center"></el-table-column>
        <el-table-column prop="appearances" label="出场数" align="center"></el-table-column>
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

      <div class="bottom-bar">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
        <el-button
          type="primary"
          icon="el-icon-data-analysis"
          class="radar-btn"
          @click="openRadarDialog"
        >
          雷达图分析
        </el-button>
      </div>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="球队">
          <el-select v-model="form.teamId" placeholder="请选择球队" :disabled="isTeamStaff">
            <el-option v-for="team in availableTeams" :key="team.id" :label="team.name" :value="team.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="球员姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="球衣号码">
          <el-input v-model="form.number"></el-input>
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.position"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="国籍">
          <el-input v-model="form.nationality"></el-input>
        </el-form-item>
        <el-form-item label="身高(米)">
          <el-input v-model="form.height"></el-input>
        </el-form-item>
        <el-form-item label="体重(公斤)">
          <el-input v-model="form.weight"></el-input>
        </el-form-item>
        <el-form-item label="惯用脚">
          <el-select v-model="form.dominantFoot" placeholder="请选择">
            <el-option label="左" value="左"></el-option>
            <el-option label="右" value="右"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="进球数">
          <el-input v-model="form.goals" type="number"></el-input>
        </el-form-item>
        <el-form-item label="助攻数">
          <el-input v-model="form.assists" type="number"></el-input>
        </el-form-item>
        <el-form-item label="出场数">
          <el-input v-model="form.appearances" type="number"></el-input>
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

    <!-- 球员雷达图分析弹窗 -->
    <el-dialog
      title="球员雷达图分析"
      :visible.sync="radarDialogVisible"
      width="900px"
    >
      <div class="radar-dialog-content">
        <el-form
          :inline="false"
          label-width="90px"
          class="radar-form"
        >
          <el-form-item label="选择球员">
            <el-select
              v-model="selectedPlayerIds"
              multiple
              filterable
              collapse-tags
              placeholder="请选择至少两个球员"
              style="width: 100%;"
            >
              <el-option
                v-for="player in allPlayerList"
                :key="player.id"
                :label="player.name"
                :value="player.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="选择维度">
            <el-checkbox-group v-model="selectedDimensions">
              <el-checkbox
                v-for="dim in dimensionOptions"
                :key="dim.value"
                :label="dim.value"
              >
                {{ dim.label }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              :loading="radarLoading"
              @click="generateRadar"
            >
              生成雷达图
            </el-button>
          </el-form-item>
        </el-form>

        <div
          v-show="showRadarChart"
          ref="radarChart"
          class="radar-chart"
        ></div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: 'Player',
  data() {
    return {
      tableData: [],
      teamList: [],
      searchForm: {
        name: '',
        teamId: null
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增球员',
      form: {},
      // 雷达图相关
      radarDialogVisible: false,
      allPlayerList: [],
      selectedPlayerIds: [],
      selectedDimensions: [],
      radarChart: null,
      radarLoading: false,
      showRadarChart: false,
      dimensionOptions: [
        { value: 'goals', label: '进球数' },
        { value: 'assists', label: '助攻数' },
        { value: 'appearances', label: '出场数' },
        { value: 'age', label: '年龄' },
        { value: 'height', label: '身高(米)' },
        { value: 'weight', label: '体重(公斤)' },
        { value: 'number', label: '球衣号码' }
      ]
    }
  },
  computed: {
    canEdit() {
      const roleStr = localStorage.getItem('userRole')
      const role = roleStr ? parseInt(roleStr) : 0
      // 游客只能查看，赛事组织者和球队工作人员可以编辑
      console.log('Player页面权限检查 - 角色:', role, '可编辑:', role !== 0)
      return role !== 0
    },
    userTeamId() {
      const teamIdStr = localStorage.getItem('userTeamId')
      return teamIdStr ? parseInt(teamIdStr) : 0
    },
    isTeamStaff() {
      const roleStr = localStorage.getItem('userRole')
      const role = roleStr ? parseInt(roleStr) : 0
      return role === 2
    },
    availableTeams() {
      // 如果是球队工作人员，只显示自己的球队
      if (this.isTeamStaff && this.userTeamId) {
        return this.teamList.filter(team => team.id === this.userTeamId)
      }
      return this.teamList
    }
  },
  mounted() {
    this.loadTeams()
    this.queryData()
  },
  beforeDestroy() {
    if (this.radarChart) {
      this.radarChart.dispose()
      this.radarChart = null
    }
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
    async queryData() {
      try {
        const res = await this.$http.post('/player/query_page', null, {
          params: {
            current: this.pagination.current,
            size: this.pagination.size,
            name: this.searchForm.name,
            teamId: this.searchForm.teamId
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
      this.searchForm.teamId = null
      this.queryData()
    },
    handleAdd() {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      this.dialogTitle = '新增球员'
      this.form = {
        status: 1,
        goals: 0,
        assists: 0,
        appearances: 0
      }
      // 如果是球队工作人员，默认设置为自己的球队
      const role = parseInt(localStorage.getItem('userRole') || '0')
      if (role === 2 && this.userTeamId) {
        this.form.teamId = this.userTeamId
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      // 如果是球队工作人员，检查是否是自己球队的球员
      const role = parseInt(localStorage.getItem('userRole') || '0')
      if (role === 2 && row.teamId !== this.userTeamId) {
        this.$message.warning('只能编辑自己球队的球员')
        return
      }
      this.dialogTitle = '编辑球员'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      if (!this.canEdit) {
        this.$message.warning('无权限操作')
        return
      }
      // 如果是球队工作人员，确保teamId是自己球队的
      if (this.isTeamStaff) {
        if (this.form.teamId !== this.userTeamId) {
          this.$message.warning('只能添加自己球队的球员')
          return
        }
      }
      try {
        const res = await this.$http.post('/player/save', this.form)
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.queryData()
        } else {
          this.$message.error(res.data.message || '保存失败')
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
      // 如果是球队工作人员，检查是否是自己球队的球员
      const role = parseInt(localStorage.getItem('userRole') || '0')
      if (role === 2 && row.teamId !== this.userTeamId) {
        this.$message.warning('只能删除自己球队的球员')
        return
      }
      this.$confirm('确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await this.$http.post('/player/delete_by_id', null, {
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
    },
    async openRadarDialog() {
      try {
        if (!this.allPlayerList.length) {
          const res = await this.$http.get('/player/all_list')
          if (res.data.code === 200) {
            this.allPlayerList = res.data.data || []
          } else {
            this.$message.error(res.data.message || '加载球员列表失败')
            return
          }
        }
      } catch (error) {
        this.$message.error('加载球员列表失败')
        return
      }
      this.selectedPlayerIds = []
      this.selectedDimensions = []
      this.showRadarChart = false
      this.radarDialogVisible = true
      this.$nextTick(() => {
        if (this.radarChart && this.$refs.radarChart) {
          this.radarChart.resize()
        }
      })
    },
    async generateRadar() {
      if (this.selectedPlayerIds.length < 2) {
        this.$message.warning('请至少选择两个球员')
        return
      }
      if (!this.selectedDimensions.length) {
        this.$message.warning('请至少选择一个维度')
        return
      }
      const payload = {
        idList: this.selectedPlayerIds,
        dimensionList: this.selectedDimensions
      }
      this.radarLoading = true
      try {
        const res = await this.$http.post('/player/echarts_analysis', payload)
        if (res.data.code !== 200) {
          this.$message.error(res.data.message || '分析失败')
          this.radarLoading = false
          return
        }
        // 后端返回的数据结构：
        // {
        //   indicator: ['进球数', '助攻数', ...],
        //   seriesData: [{ name: '张三', value: [15, 5, ...] }, ...]
        // }
        const analysis = res.data.data || {}
        const indicator = Array.isArray(analysis.indicator) ? analysis.indicator : []
        const seriesData = Array.isArray(analysis.seriesData) ? analysis.seriesData : []
        if (!indicator.length || !seriesData.length) {
          this.$message.warning('未获取到分析数据')
          this.radarLoading = false
          return
        }
        this.drawRadarChart(indicator, seriesData)
      } catch (error) {
        this.$message.error('分析失败')
      } finally {
        this.radarLoading = false
      }
    },
    drawRadarChart(indicatorLabels, seriesData) {
      if (!this.$refs.radarChart) {
        return
      }
      if (!this.radarChart) {
        this.radarChart = echarts.init(this.$refs.radarChart)
      }
      // 根据后端返回的 seriesData 计算每个维度最大值
      const dimCount = indicatorLabels.length
      const maxArr = new Array(dimCount).fill(0)
      seriesData.forEach(s => {
        (s.value || []).forEach((v, idx) => {
          const num = v == null ? 0 : Number(v)
          if (!Number.isNaN(num)) {
            maxArr[idx] = Math.max(maxArr[idx], num)
          }
        })
      })

      const indicators = indicatorLabels.map((label, idx) => ({
        name: label,
        max: Math.max(maxArr[idx] || 0, 1)
      }))

      const option = {
        tooltip: {},
        legend: {
          data: seriesData.map(s => s.name),
          top: 10 // 图例放上面，给下面留空间
        },
        radar: {
          indicator: indicators,
          // 垂直略偏下，让上、下两侧文字都留出空间
          center: ['50%', '55%'],
          radius: '65%',
          splitNumber: 5,
          name: {
            textStyle: {
              color: '#333',
              fontSize: 12
            }
          }
        },
        series: [
          {
            type: 'radar',
            data: seriesData,
            areaStyle: { opacity: 0.15 }
          }
        ]
      }
      this.radarChart.setOption(option)
      this.radarChart.resize()
      this.showRadarChart = true
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

.bottom-bar {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.radar-btn {
  margin-left: 16px;
}

.radar-dialog-content {
  display: flex;
  flex-direction: column;
  padding-top: 4px;
}

.radar-form {
  margin-bottom: 8px;
}

.radar-chart {
  width: 700px;
  max-width: 100%;
  height: 520px;
  margin: 12px auto;
  background: #fafafa;
  border-radius: 8px;
  padding: 20px;
  box-sizing: border-box;
}
</style>

