
<template>
  <div class="calendar-view">
    <el-calendar v-model="currentDate">
      <template #date-cell="{ data }">
        <div class="date-cell" :class="getDayClass(data.day)" @click="handleDateClick(data.day)">
          <span class="date-number">{{ data.day.split('-')[2] }}</span>
          <div v-if="getDayData(data.day)" class="events">
            <el-popover placement="top" trigger="hover" width="200">
              <template #reference>
                <el-tag size="small" :type="getTagType(getDayData(data.day).phase)">{{ getDayData(data.day).phase_display }}</el-tag>
              </template>
              <div>
                <p><strong>日期:</strong> {{ data.day }}</p>
                <p><strong>阶段:</strong> {{ getDayData(data.day).phase_display }}</p>
                <!-- More details can be added here -->
              </div>
            </el-popover>
          </div>
        </div>
      </template>
    </el-calendar>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { useCycleStore } from '@/stores/cycle';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';

const currentDate = ref(new Date());
const cycleStore = useCycleStore();
const router = useRouter();

const calendarData = computed(() => cycleStore.calendarView);

const formattedMonth = computed(() => {
  return dayjs(currentDate.value).format('YYYY-MM');
});

onMounted(() => {
  cycleStore.fetchCalendarView(formattedMonth.value);
});

watch(currentDate, (newDate) => {
  const newMonth = dayjs(newDate).format('YYYY-MM');
  if (newMonth !== formattedMonth.value) {
    cycleStore.fetchCalendarView(newMonth);
  }
});

function getDayData(day: string) {
  return calendarData.value[day];
}

function getDayClass(day: string) {
  const dayData = getDayData(day);
  if (!dayData) return '';
  return `phase-${dayData.phase.toLowerCase()}`;
}

function getTagType(phase: string) {
  switch (phase) {
    case 'MENSTRUAL':
      return 'danger';
    case 'OVULATION':
      return 'warning';
    case 'LUTEAL':
      return 'info';
    default:
      return 'success';
  }
}

function handleDateClick(day: string) {
  router.push(`/record/${day}`);
}
</script>

<style scoped>
.calendar-view {
  padding: 20px;
}

.date-cell {
  padding: 8px;
  height: 100%;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.date-cell:hover {
  background-color: #f5f7fa;
}

.phase-menstrual {
  background-color: #fef0f0;
}

.phase-ovulation {
  background-color: #fdf6ec;
}

.phase-luteal {
  background-color: #f4f4f5;
}

.date-number {
  font-size: 14px;
}

.events {
  margin-top: 4px;
}
</style>
