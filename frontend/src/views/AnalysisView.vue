<template>
  <div class="analysis-view">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据分析</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="loadAnalysis"
          />
        </div>
      </template>

      <div class="analysis-content">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-value">{{ stats.totalCycles || 0 }}</div>
              <div class="stat-label">总周期数</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-value">{{ stats.averageCycleLength || 28 }}天</div>
              <div class="stat-label">平均周期长度</div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card">
              <div class="stat-value">{{ stats.averagePeriodLength || 5 }}天</div>
              <div class="stat-label">平均经期长度</div>
            </el-card>
          </el-col>
        </el-row>

        <el-card class="chart-card">
          <template #header>
            <span>周期趋势图</span>
          </template>
          <div class="chart-container">
            <v-chart :option="cycleChartOption" style="height: 400px" />
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useCycleStore } from '@/stores/cycle'
import dayjs from 'dayjs'

const cycleStore = useCycleStore()
const dateRange = ref([dayjs().subtract(6, 'months').toDate(), dayjs().toDate()])
const stats = ref({})

const cycleChartOption = computed(() => ({
  title: {
    text: '周期记录趋势'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: cycleStore.cycles.map(c => c.startDate)
  },
  yAxis: {
    type: 'value',
    name: '周期长度（天）'
  },
  series: [{
    data: cycleStore.cycles.map(c => c.cycleLength || 28),
    type: 'line',
    smooth: true,
    itemStyle: {
      color: '#ff6b6b'
    }
  }]
}))

const loadAnalysis = async () => {
  // 这里可以加载分析数据
  await cycleStore.fetchCycles()
  
  if (cycleStore.cycles.length > 0) {
    const cycleLengths = cycleStore.cycles.filter(c => c.cycleLength).map(c => c.cycleLength!)
    const periodLengths = cycleStore.cycles.filter(c => c.periodLength).map(c => c.periodLength!)
    
    stats.value = {
      totalCycles: cycleStore.cycles.length,
      averageCycleLength: cycleLengths.length > 0 ? Math.round(cycleLengths.reduce((a, b) => a + b, 0) / cycleLengths.length) : 28,
      averagePeriodLength: periodLengths.length > 0 ? Math.round(periodLengths.reduce((a, b) => a + b, 0) / periodLengths.length) : 5
    }
  }
}

onMounted(() => {
  loadAnalysis()
})
</script>

<style scoped>
.analysis-view {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.analysis-content {
  margin-top: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #ff6b6b;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.chart-card {
  margin-top: 20px;
}

.chart-container {
  padding: 20px 0;
}
</style>