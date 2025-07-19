
<template>
  <div ref="chart" style="width: 100%; height: 400px;"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, shallowRef } from 'vue';
import * as echarts from 'echarts';
import apiClient from '@/utils/api';

const chart = ref(null);
const myChart = shallowRef(null);

onMounted(async () => {
  const response = await apiClient.get('/analysis/trends');
  const data = response.data;

  myChart.value = echarts.init(chart.value);
  myChart.value.setOption({
    xAxis: {
      type: 'category',
      data: data.cycle_length_trend.labels,
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        data: data.cycle_length_trend.values,
        type: 'line',
      },
    ],
  });
});
</script>
