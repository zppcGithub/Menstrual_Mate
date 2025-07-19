
<template>
  <div class="record-view">
    <h2>记录 {{ selectedDate }}</h2>
    <el-form :model="recordForm" label-width="120px">
      <el-form-item label="经血流量">
        <el-select v-model="recordForm.menstrualFlow" placeholder="选择流量">
          <el-option label="点滴" value="SPOTTING"></el-option>
          <el-option label="少量" value="LIGHT"></el-option>
          <el-option label="中等" value="MEDIUM"></el-option>
          <el-option label="大量" value="HEAVY"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="症状">
        <el-checkbox-group v-model="recordForm.symptoms">
          <el-checkbox v-for="symptom in cycleStore.symptoms" :key="symptom.id" :label="symptom.id">
            {{ symptom.name }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="体温">
        <el-input-number v-model="recordForm.temperature" :precision="1" :step="0.1"></el-input-number>
      </el-form-item>
      <el-form-item label="体重">
        <el-input-number v-model="recordForm.weight" :precision="2" :step="0.1"></el-input-number>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="recordForm.notes" type="textarea"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="saveRecord">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useCycleStore } from '@/stores/cycle';
import dayjs from 'dayjs';

const route = useRoute();
const router = useRouter();
const cycleStore = useCycleStore();

const selectedDate = computed(() => route.params.date || dayjs().format('YYYY-MM-DD'));

const recordForm = ref({
  recordDate: selectedDate.value,
  menstrualFlow: '',
  symptoms: [],
  temperature: null,
  weight: null,
  notes: '',
});

onMounted(async () => {
  await cycleStore.fetchDailyRecord(selectedDate.value);
  await cycleStore.fetchSymptoms();
  if (cycleStore.dailyRecord) {
    recordForm.value = { ...cycleStore.dailyRecord };
  }
});

function saveRecord() {
  cycleStore.saveDailyRecord(recordForm.value);
  router.push('/');
}

function cancel() {
  router.push('/');
}
</script>

<style scoped>
.record-view {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}
</style>
