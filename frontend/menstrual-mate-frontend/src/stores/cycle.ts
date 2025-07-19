import { defineStore } from 'pinia';
import apiClient from '../utils/api';

export const useCycleStore = defineStore('cycle', {
  state: () => ({
    calendarView: {},
    dailyRecord: null,
    symptoms: [],
    loading: false,
  }),

  getters: {
    isLoading: (state) => state.loading,
  },

  actions: {
    async fetchCalendarView(yearMonth: string) {
      this.loading = true;
      try {
        const response = await apiClient.get(`/calendar/view?yearMonth=${yearMonth}`);
        this.calendarView = response.data;
      } catch (error) {
        console.error('Failed to fetch calendar view:', error);
      } finally {
        this.loading = false;
      }
    },

    async fetchDailyRecord(date: string) {
      this.loading = true;
      try {
        const response = await apiClient.get(`/daily-records/date/${date}`);
        this.dailyRecord = response.data;
      } catch (error) {
        console.error('Failed to fetch daily record:', error);
        this.dailyRecord = null;
      } finally {
        this.loading = false;
      }
    },

    async saveDailyRecord(record: any) {
      this.loading = true;
      try {
        await apiClient.post('/daily-records', record);
        // Optionally, you can re-fetch data or navigate
      } catch (error) {
        console.error('Failed to save daily record:', error);
      } finally {
        this.loading = false;
      }
    },

    async fetchSymptoms() {
      this.loading = true;
      try {
        const response = await apiClient.get('/symptoms');
        this.symptoms = response.data;
      } catch (error) {
        console.error('Failed to fetch symptoms:', error);
      } finally {
        this.loading = false;
      }
    },
  },
});