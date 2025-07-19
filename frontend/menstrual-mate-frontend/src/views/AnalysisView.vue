
<template>
  <div class="analysis-view">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>周期长度趋势</template>
          <CycleLengthChart />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>经期长度分析</template>
          <PeriodLengthChart />
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>症状频率统计</template>
          <SymptomFrequencyChart />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import CycleLengthChart from '@/components/charts/CycleLengthChart.vue';
import PeriodLengthChart from '@/components/charts/PeriodLengthChart.vue';
import SymptomFrequencyChart from '@/components/charts/SymptomFrequencyChart.vue';
</script>

<style scoped>
.analysis-view {
  padding: 20px;
}
</style>
