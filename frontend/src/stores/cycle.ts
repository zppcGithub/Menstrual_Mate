import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { CycleRecord, DailyRecord } from '@/types'
import api from '@/utils/api'

export const useCycleStore = defineStore('cycle', () => {
  const cycles = ref<CycleRecord[]>([])
  const dailyRecords = ref<DailyRecord[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const fetchCycles = async () => {
    loading.value = true
    try {
      const response = await api.get('/cycles')
      cycles.value = response.data.data
    } catch (err) {
      error.value = '获取周期记录失败'
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  const createCycle = async (cycle: Partial<CycleRecord>) => {
    try {
      const response = await api.post('/cycles', cycle)
      await fetchCycles()
      return response.data.data
    } catch (err) {
      error.value = '创建周期记录失败'
      throw err
    }
  }

  const fetchDailyRecords = async (startDate?: string, endDate?: string) => {
    loading.value = true
    try {
      const params = startDate && endDate ? { startDate, endDate } : {}
      const response = await api.get('/daily-records', { params })
      dailyRecords.value = response.data.data
    } catch (err) {
      error.value = '获取每日记录失败'
      console.error(err)
    } finally {
      loading.value = false
    }
  }

  const createDailyRecord = async (record: Partial<DailyRecord>) => {
    try {
      const response = await api.post('/daily-records', record)
      await fetchDailyRecords()
      return response.data.data
    } catch (err) {
      error.value = '创建每日记录失败'
      throw err
    }
  }

  const getCalendarView = async (yearMonth: string) => {
    try {
      const response = await api.get('/calendar/view', {
        params: { yearMonth }
      })
      return response.data.data
    } catch (err) {
      error.value = '获取日历视图失败'
      throw err
    }
  }

  return {
    cycles,
    dailyRecords,
    loading,
    error,
    fetchCycles,
    createCycle,
    fetchDailyRecords,
    createDailyRecord,
    getCalendarView
  }
})