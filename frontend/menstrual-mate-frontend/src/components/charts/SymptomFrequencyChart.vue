
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
  const response = await apiClient.get('/analysis/symptom-frequency');
  const data = response.data;

  myChart.value = echarts.init(chart.value);
  myChart.value.setOption({
    tooltip: {
        trigger: 'item'
    },
    series: [
        {
            name: 'Symptom Frequency',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '20',
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: data.symptom_frequency
        }
    ]
});
});
</script>
