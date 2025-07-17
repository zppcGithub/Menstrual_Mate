<template>
  <div class="calendar-view">
    <div class="calendar-header">
      <h2>{{ currentMonth.format('YYYY年MM月') }}</h2>
      <div class="calendar-controls">
        <el-button-group>
          <el-button @click="previousMonth" :icon="ArrowLeft" />
          <el-button @click="goToToday" type="primary">今天</el-button>
          <el-button @click="nextMonth" :icon="ArrowRight" />
        </el-button-group>
      </div>
    </div>

    <div class="calendar-grid">
      <div class="weekday-headers">
        <div v-for="day in weekdays" :key="day" class="weekday">
          {{ day }}
        </div>
      </div>
      
      <div class="calendar-days">
        <div 
          v-for="day in calendarDays" 
          :key="day.date"
          class="calendar-day"
          :class="{
            'today': day.isToday,
            'other-month': !day.isCurrentMonth,
            'menstrual': day.menstrualFlow,
            'predicted': day.isPredicted
          }"
          @click="selectDay(day)"
        >
          <div class="day-number">{{ day.day }}</div>
          <div class="day-content">
            <div v-if="day.menstrualFlow" class="flow-indicator" :class="day.menstrualFlow">
              {{ flowEmoji[day.menstrualFlow] }}
            </div>
            <div v-if="day.symptoms.length" class="symptoms-count">
              {{ day.symptoms.length }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="dayDetailVisible" title="今日详情" width="400px">
      <div class="day-detail">
        <h3>{{ selectedDay?.date.format('YYYY年MM月DD日') }}</h3>
        <div v-if="selectedDayRecord">
          <p>经期流量: {{ flowText[selectedDayRecord.menstrualFlow] || '无' }}</p>
          <p>基础体温: {{ selectedDayRecord.temperature || '未记录' }}°C</p>
          <p>体重: {{ selectedDayRecord.weight || '未记录' }}kg</p>
          <p>症状: {{ selectedDayRecord.symptoms?.join(', ') || '无' }}</p>
          <p>备注: {{ selectedDayRecord.notes || '无' }}</p>
        </div>
        <div v-else>
          <p>今日暂无记录</p>
        </div>
      </div>
      <template #footer>
        <el-button @click="dayDetailVisible = false">关闭</el-button>
        <el-button type="primary" @click="addRecord">添加记录</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCycleStore } from '@/stores/cycle'
import dayjs from 'dayjs'
import type { DailyRecord } from '@/types'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

const router = useRouter()
const cycleStore = useCycleStore()

const currentMonth = ref(dayjs())
const selectedDay = ref<any>(null)
const selectedDayRecord = ref<DailyRecord | null>(null)
const dayDetailVisible = ref(false)

const weekdays = ['日', '一', '二', '三', '四', '五', '六']

const flowEmoji = {
  'SPOTTING': '•',
  'LIGHT': '◦',
  'MEDIUM': '●',
  'HEAVY': '◉'
}

const flowText = {
  'SPOTTING': '点滴',
  'LIGHT': '少量',
  'MEDIUM': '中等',
  'HEAVY': '大量'
}

const calendarDays = computed(() => {
  const startOfMonth = currentMonth.value.startOf('month')
  const endOfMonth = currentMonth.value.endOf('month')
  const startOfCalendar = startOfMonth.startOf('week')
  const endOfCalendar = endOfMonth.endOf('week')

  const days = []
  let currentDay = startOfCalendar

  while (currentDay.isBefore(endOfCalendar.add(1, 'day'))) {
    const dailyRecord = cycleStore.dailyRecords.find(
      record => record.recordDate === currentDay.format('YYYY-MM-DD')
    )

    days.push({
      date: currentDay.clone(),
      day: currentDay.date(),
      isCurrentMonth: currentDay.month() === currentMonth.value.month(),
      isToday: currentDay.isSame(dayjs(), 'day'),
      menstrualFlow: dailyRecord?.menstrualFlow,
      symptoms: dailyRecord?.symptoms || [],
      isPredicted: false // 后续实现预测功能
    })

    currentDay = currentDay.add(1, 'day')
  }

  return days
})

const previousMonth = () => {
  currentMonth.value = currentMonth.value.subtract(1, 'month')
  loadCalendarData()
}

const nextMonth = () => {
  currentMonth.value = currentMonth.value.add(1, 'month')
  loadCalendarData()
}

const goToToday = () => {
  currentMonth.value = dayjs()
  loadCalendarData()
}

const selectDay = (day: any) => {
  selectedDay.value = day
  selectedDayRecord.value = cycleStore.dailyRecords.find(
    record => record.recordDate === day.date.format('YYYY-MM-DD')
  ) || null
  dayDetailVisible.value = true
}

const addRecord = () => {
  dayDetailVisible.value = false
  router.push('/record')
}

const loadCalendarData = async () => {
  const startDate = currentMonth.value.startOf('month').format('YYYY-MM-DD')
  const endDate = currentMonth.value.endOf('month').format('YYYY-MM-DD')
  
  await Promise.all([
    cycleStore.fetchCycles(),
    cycleStore.fetchDailyRecords(startDate, endDate)
  ])
}

onMounted(() => {
  loadCalendarData()
})
</script>

<style scoped>
.calendar-view {
  max-width: 1200px;
  margin: 0 auto;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.calendar-header h2 {
  color: #333;
  margin: 0;
}

.calendar-grid {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.weekday-headers {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  background: #f5f5f5;
  border-bottom: 1px solid #e0e0e0;
}

.weekday {
  padding: 12px;
  text-align: center;
  font-weight: bold;
  color: #666;
}

.calendar-days {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  min-height: 400px;
}

.calendar-day {
  border: 1px solid #f0f0f0;
  padding: 8px;
  min-height: 80px;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
}

.calendar-day:hover {
  background: #f9f9f9;
  transform: scale(1.02);
}

.calendar-day.today {
  background: #fff3e0;
  border-color: #ff9800;
}

.calendar-day.other-month {
  opacity: 0.5;
}

.calendar-day.menstrual {
  background: #ffebee;
}

.day-number {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 4px;
}

.day-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.flow-indicator {
  font-size: 18px;
  font-weight: bold;
}

.flow-indicator.SPOTTING { color: #ff9800; }
.flow-indicator.LIGHT { color: #ff5722; }
.flow-indicator.MEDIUM { color: #f44336; }
.flow-indicator.HEAVY { color: #d32f2f; }

.symptoms-count {
  background: #4caf50;
  color: white;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
}

.day-detail h3 {
  margin-top: 0;
  color: #333;
}

.day-detail p {
  margin: 8px 0;
  color: #666;
}

@media (max-width: 768px) {
  .calendar-day {
    min-height: 60px;
    padding: 4px;
  }
  
  .weekday {
    padding: 8px 4px;
    font-size: 12px;
  }
  
  .day-number {
    font-size: 12px;
  }
  
  .flow-indicator {
    font-size: 14px;
  }
}
</style>