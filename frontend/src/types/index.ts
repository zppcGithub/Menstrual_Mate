export interface CycleRecord {
  id: number
  startDate: string
  endDate?: string
  cycleLength?: number
  periodLength?: number
  createdAt: string
  updatedAt: string
}

export interface DailyRecord {
  id: number
  recordDate: string
  menstrualFlow?: 'SPOTTING' | 'LIGHT' | 'MEDIUM' | 'HEAVY'
  temperature?: number
  weight?: number
  notes?: string
  createdAt: string
  updatedAt: string
  symptoms?: SymptomRecord[]
}

export interface Symptom {
  id: number
  name: string
  type: 'SYMPTOM' | 'MOOD' | 'ACTIVITY'
  icon?: string
  color: string
  isCustom: boolean
}

export interface SymptomRecord {
  symptomId: number
  symptomName: string
  symptomType: string
  intensity: number
  icon?: string
  color: string
}

export interface HealthTip {
  id: number
  phase: 'MENSTRUAL' | 'FOLLICULAR' | 'OVULATION' | 'LUTEAL'
  title: string
  content: string
  category: 'HEALTH' | 'DIET' | 'EXERCISE' | 'SKINCARE'
  priority: number
  createdAt: string
}

export interface CalendarDay {
  date: string
  phase?: string
  menstrualFlow?: string
  symptoms: string[]
  isPredicted: boolean
  isToday: boolean
}

export interface CalendarViewData {
  month: string
  year: number
  days: CalendarDay[]
  cycles: CycleRecord[]
}

export interface ApiResponse<T> {
  success: boolean
  message?: string
  data: T
}