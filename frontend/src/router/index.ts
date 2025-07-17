import { createRouter, createWebHistory } from 'vue-router'
import CalendarView from '@/views/CalendarView.vue'
import RecordView from '@/views/RecordView.vue'
import AnalysisView from '@/views/AnalysisView.vue'
import SettingsView from '@/views/SettingsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'calendar',
      component: CalendarView,
      meta: {
        title: '日历视图',
        icon: 'Calendar'
      }
    },
    {
      path: '/record',
      name: 'record',
      component: RecordView,
      meta: {
        title: '记录管理',
        icon: 'Edit'
      }
    },
    {
      path: '/analysis',
      name: 'analysis',
      component: AnalysisView,
      meta: {
        title: '数据分析',
        icon: 'TrendCharts'
      }
    },
    {
      path: '/settings',
      name: 'settings',
      component: SettingsView,
      meta: {
        title: '设置',
        icon: 'Setting'
      }
    }
  ]
})

export default router