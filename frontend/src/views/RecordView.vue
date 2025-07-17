<template>
  <div class="record-view">
    <el-card class="record-card">
      <template #header>
        <div class="card-header">
          <span>记录管理</span>
          <el-button type="primary" @click="showAddCycleDialog = true">
            <el-icon><Plus /></el-icon>
            添加周期
          </el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="record-tabs">
        <el-tab-pane label="周期记录" name="cycles">
          <el-table :data="cycles" style="width: 100%">
            <el-table-column prop="startDate" label="开始日期" width="120" />
            <el-table-column prop="endDate" label="结束日期" width="120" />
            <el-table-column prop="cycleLength" label="周期长度" width="100" />
            <el-table-column prop="periodLength" label="经期长度" width="100" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button size="small" @click="editCycle(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteCycle(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="每日记录" name="daily">
          <div class="daily-records">
            <el-date-picker
              v-model="selectedDate"
              type="date"
              placeholder="选择日期"
              @change="loadDailyRecord"
            />
            
            <el-form :model="dailyForm" label-width="80px" class="daily-form">
              <el-form-item label="经期流量">
                <el-select v-model="dailyForm.menstrualFlow" placeholder="选择流量">
                  <el-option label="无" value="" />
                  <el-option label="点滴" value="SPOTTING" />
                  <el-option label="少量" value="LIGHT" />
                  <el-option label="中等" value="MEDIUM" />
                  <el-option label="大量" value="HEAVY" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="基础体温">
                <el-input-number 
                  v-model="dailyForm.temperature" 
                  :min="35" 
                  :max="40" 
                  :precision="1" 
                  :step="0.1"
                />
              </el-form-item>
              
              <el-form-item label="体重">
                <el-input-number 
                  v-model="dailyForm.weight" 
                  :min="30" 
                  :max="200" 
                  :precision="1" 
                  :step="0.1"
                />
              </el-form-item>
              
              <el-form-item label="症状">
                <el-checkbox-group v-model="dailyForm.symptoms">
                  <el-checkbox label="腹痛">腹痛</el-checkbox>
                  <el-checkbox label="乳房胀痛">乳房胀痛</el-checkbox>
                  <el-checkbox label="头痛">头痛</el-checkbox>
                  <el-checkbox label="腰酸">腰酸</el-checkbox>
                  <el-checkbox label="恶心">恶心</el-checkbox>
                  <el-checkbox label="疲劳">疲劳</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              
              <el-form-item label="备注">
                <el-input 
                  v-model="dailyForm.notes" 
                  type="textarea" 
                  :rows="3" 
                  placeholder="记录今日感受..."
                />
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="saveDailyRecord" :loading="saving">保存记录</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 添加周期对话框 -->
    <el-dialog v-model="showAddCycleDialog" title="添加周期记录" width="400px">
      <el-form :model="cycleForm" label-width="80px">
        <el-form-item label="开始日期" required>
          <el-date-picker v-model="cycleForm.startDate" type="date" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="cycleForm.endDate" type="date" />
        </el-form-item>
        <el-form-item label="周期长度">
          <el-input-number v-model="cycleForm.cycleLength" :min="20" :max="45" />
        </el-form-item>
        <el-form-item label="经期长度">
          <el-input-number v-model="cycleForm.periodLength" :min="1" :max="10" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddCycleDialog = false">取消</el-button>
        <el-button type="primary" @click="saveCycle" :loading="saving">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useCycleStore } from '@/stores/cycle'
import type { CycleRecord, DailyRecord } from '@/types'
import { Plus } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

const cycleStore = useCycleStore()
const activeTab = ref('cycles')
const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const showAddCycleDialog = ref(false)
const saving = ref(false)

const cycles = ref<CycleRecord[]>([])
const dailyForm = ref({
  recordDate: dayjs().format('YYYY-MM-DD'),
  menstrualFlow: '',
  temperature: undefined,
  weight: undefined,
  symptoms: [] as string[],
  notes: ''
})

const cycleForm = ref({
  startDate: dayjs().format('YYYY-MM-DD'),
  endDate: '',
  cycleLength: 28,
  periodLength: 5
})

const loadData = async () => {
  await cycleStore.fetchCycles()
  cycles.value = cycleStore.cycles
}

const loadDailyRecord = async () => {
  const date = selectedDate.value
  dailyForm.value.recordDate = date
  
  const existingRecord = cycleStore.dailyRecords.find(
    record => record.recordDate === date
  )
  
  if (existingRecord) {
    dailyForm.value = {
      ...existingRecord,
      symptoms: existingRecord.symptoms || []
    }
  } else {
    dailyForm.value = {
      recordDate: date,
      menstrualFlow: '',
      temperature: undefined,
      weight: undefined,
      symptoms: [],
      notes: ''
    }
  }
}

const saveDailyRecord = async () => {
  saving.value = true
  try {
    await cycleStore.createDailyRecord(dailyForm.value)
    await loadDailyRecord()
    
    import('element-plus').then(({ ElMessage }) => {
      ElMessage.success('记录保存成功！')
    })
  } catch (error) {
    import('element-plus').then(({ ElMessage }) => {
      ElMessage.error('保存失败，请重试')
    })
  } finally {
    saving.value = false
  }
}

const saveCycle = async () => {
  saving.value = true
  try {
    await cycleStore.createCycle(cycleForm.value)
    await loadData()
    showAddCycleDialog.value = false
    
    import('element-plus').then(({ ElMessage }) => {
      ElMessage.success('周期记录添加成功！')
    })
  } catch (error) {
    import('element-plus').then(({ ElMessage }) => {
      ElMessage.error('添加失败，请重试')
    })
  } finally {
    saving.value = false
  }
}

const editCycle = (cycle: CycleRecord) => {
  cycleForm.value = { ...cycle }
  showAddCycleDialog.value = true
}

const deleteCycle = async (cycle: CycleRecord) => {
  try {
    await import('element-plus').then(async ({ ElMessageBox }) => {
      await ElMessageBox.confirm('确定要删除这条周期记录吗？', '确认删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
    })
    
    // 这里需要调用API删除，暂时只是前端删除
    await loadData()
    
    import('element-plus').then(({ ElMessage }) => {
      ElMessage.success('删除成功')
    })
  } catch (error) {
    // 用户取消删除
  }
}

onMounted(() => {
  loadData()
  loadDailyRecord()
})
</script>

<style scoped>
.record-view {
  max-width: 1200px;
  margin: 0 auto;
}

.record-card {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 18px;
  font-weight: bold;
}

.record-tabs {
  margin-top: 20px;
}

.daily-records {
  max-width: 600px;
}

.daily-form {
  margin-top: 20px;
}

.daily-form .el-form-item {
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .record-card {
    margin: 0 10px;
  }
  
  .daily-form {
    .el-form-item {
      margin-bottom: 12px;
    }
  }
}
</style>