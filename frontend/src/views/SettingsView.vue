<template>
  <div class="settings-view">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>设置</span>
        </div>
      </template>
      
      <div class="settings-content">
        <el-form :model="settingsForm" label-width="120px">
          <el-form-item label="应用主题">
            <el-switch
              v-model="settingsForm.darkTheme"
              active-text="深色"
              inactive-text="浅色"
              @change="toggleTheme"
            />
          </el-form-item>
          
          <el-form-item label="默认周期长度">
            <el-input-number
              v-model="settingsForm.defaultCycleLength"
              :min="20"
              :max="45"
              :step="1"
            />
          </el-form-item>
          
          <el-form-item label="默认经期长度">
            <el-input-number
              v-model="settingsForm.defaultPeriodLength"
              :min="1"
              :max="10"
              :step="1"
            />
          </el-form-item>
          
          <el-form-item label="提醒设置">
            <el-checkbox v-model="settingsForm.enableReminders">
              开启经期提醒
            </el-checkbox>
          </el-form-item>
          
          <el-form-item label="数据备份">
            <el-button @click="exportData" type="primary">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
            <el-button @click="importData" type="primary">
              <el-icon><Upload /></el-icon>
              导入数据
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="backup-section">
          <h3>数据管理</h3>
          <p>您可以导出或导入您的周期记录和每日记录数据。</p>
          
          <div class="backup-actions">
            <el-button @click="exportToCSV" type="success">
              导出为CSV
            </el-button>
            <el-button @click="clearAllData" type="danger">
              清空所有数据
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- 文件上传对话框 -->
    <el-dialog v-model="importDialogVisible" title="导入数据" width="400px">
      <el-upload
        ref="uploadRef"
        class="upload-demo"
        drag
        :auto-upload="false"
        :on-change="handleFileChange"
        accept=".json,.csv"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          拖拽文件到此处或 <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">支持 .json 和 .csv 格式文件</div>
        </template>
      </el-upload>
      
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmImport">确认导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Download, Upload, UploadFilled } from '@element-plus/icons-vue'

const settingsForm = ref({
  darkTheme: false,
  defaultCycleLength: 28,
  defaultPeriodLength: 5,
  enableReminders: true
})

const importDialogVisible = ref(false)
const uploadRef = ref()
const importFile = ref<File | null>(null)

const toggleTheme = (dark: boolean) => {
  document.documentElement.classList.toggle('dark', dark)
  localStorage.setItem('theme', dark ? 'dark' : 'light')
  saveSettings()
}

const saveSettings = () => {
  localStorage.setItem('settings', JSON.stringify(settingsForm.value))
  
  import('element-plus').then(({ ElMessage }) => {
    ElMessage.success('设置已保存')
  })
}

const exportData = () => {
  const cycles = JSON.parse(localStorage.getItem('cycles') || '[]')
  const dailyRecords = JSON.parse(localStorage.getItem('dailyRecords') || '[]')
  
  const data = {
    cycles,
    dailyRecords,
    exportDate: new Date().toISOString()
  }
  
  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `menstrual-mate-backup-${new Date().toISOString().split('T')[0]}.json`
  a.click()
  URL.revokeObjectURL(url)
  
  import('element-plus').then(({ ElMessage }) => {
    ElMessage.success('数据导出成功')
  })
}

const importData = () => {
  importDialogVisible.value = true
}

const handleFileChange = (file: any) => {
  importFile.value = file.raw
}

const confirmImport = async () => {
  if (!importFile.value) return
  
  try {
    const text = await importFile.value.text()
    const data = JSON.parse(text)
    
    if (data.cycles) {
      localStorage.setItem('cycles', JSON.stringify(data.cycles))
    }
    if (data.dailyRecords) {
      localStorage.setItem('dailyRecords', JSON.stringify(data.dailyRecords))
    }
    
    importDialogVisible.value = false
    
    import('element-plus').then(({ ElMessage }) => {
      ElMessage.success('数据导入成功')
    })
  } catch (error) {
    import('element-plus').then(({ ElMessage }) => {
      ElMessage.error('导入失败，请检查文件格式')
    })
  }
}

const exportToCSV = () => {
  const cycles = JSON.parse(localStorage.getItem('cycles') || '[]')
  const dailyRecords = JSON.parse(localStorage.getItem('dailyRecords') || '[]')
  
  let csvContent = 'type,start_date,end_date,cycle_length,period_length,temperature,weight,notes\n'
  
  cycles.forEach((cycle: any) => {
    csvContent += `cycle,${cycle.startDate},${cycle.endDate || ''},${cycle.cycleLength || ''},${cycle.periodLength || ''},,,\n`
  })
  
  dailyRecords.forEach((record: any) => {
    csvContent += `daily,${record.recordDate},,${record.menstrualFlow || ''},,${record.temperature || ''},${record.weight || ''},"${record.notes || ''}"\n`
  })
  
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `menstrual-mate-data-${new Date().toISOString().split('T')[0]}.csv`
  a.click()
  URL.revokeObjectURL(url)
}

const clearAllData = () => {
  import('element-plus').then(async ({ ElMessageBox }) => {
    try {
      await ElMessageBox.confirm(
        '确定要清空所有数据吗？此操作不可恢复！',
        '确认清空',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      
      localStorage.removeItem('cycles')
      localStorage.removeItem('dailyRecords')
      localStorage.removeItem('settings')
      
      import('element-plus').then(({ ElMessage }) => {
        ElMessage.success('所有数据已清空')
      })
    } catch (error) {
      // 用户取消操作
    }
  })
}

onMounted(() => {
  const savedSettings = localStorage.getItem('settings')
  if (savedSettings) {
    settingsForm.value = JSON.parse(savedSettings)
  }
  
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme === 'dark') {
    settingsForm.value.darkTheme = true
    document.documentElement.classList.add('dark')
  }
})
</script>

<style scoped>
.settings-view {
  max-width: 800px;
  margin: 0 auto;
}

.settings-content {
  padding: 20px;
}

.settings-content .el-form-item {
  margin-bottom: 24px;
}

.backup-section {
  margin-top: 40px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.backup-section h3 {
  margin-top: 0;
  color: #333;
}

.backup-section p {
  color: #666;
  margin-bottom: 16px;
}

.backup-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.upload-demo {
  text-align: center;
}

@media (max-width: 768px) {
  .backup-actions {
    flex-direction: column;
  }
}
</style>