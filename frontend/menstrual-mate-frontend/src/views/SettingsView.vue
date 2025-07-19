
<template>
  <div class="settings-view">
    <h2>设置</h2>
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>个性化主题</span>
        </div>
      </template>
      <el-form>
        <el-form-item label="选择主题颜色">
          <el-color-picker v-model="themeColor" @change="changeTheme" />
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>数据管理</span>
        </div>
      </template>
      <el-button type="primary" @click="backupData">备份数据</el-button>
      <el-upload
        action="/api/data/restore"
        :show-file-list="false"
        :on-success="handleRestoreSuccess"
      >
        <el-button type="success">恢复数据</el-button>
      </el-upload>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';

const themeColor = ref('#409EFF');

function changeTheme(color: string) {
  document.documentElement.style.setProperty('--el-color-primary', color);
}

function backupData() {
  window.open('/api/data/backup');
}

function handleRestoreSuccess() {
  ElMessage.success('数据恢复成功!');
}
</script>

<style scoped>
.settings-view {
  padding: 20px;
}
.box-card {
  margin-bottom: 20px;
}
</style>
