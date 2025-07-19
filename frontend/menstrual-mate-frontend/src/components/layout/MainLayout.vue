
<template>
  <el-container class="main-layout">
    <el-aside width="200px">
      <div class="logo-container">
        <img alt="App logo" class="logo" src="@/assets/logo.svg" />
        <span>Menstrual Mate</span>
      </div>
      <el-menu
        :default-active="activeRoute"
        class="el-menu-vertical-demo"
        router
      >
        <el-menu-item index="/">
          <el-icon><icon-calendar /></el-icon>
          <span>日历视图</span>
        </el-menu-item>
        <el-menu-item index="/record">
          <el-icon><icon-edit /></el-icon>
          <span>数据记录</span>
        </el-menu-item>
        <el-menu-item index="/analysis">
          <el-icon><icon-data-line /></el-icon>
          <span>分析中心</span>
        </el-menu-item>
        <el-menu-item index="/settings">
          <el-icon><icon-setting /></el-icon>
          <span>设置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import {
  Calendar as IconCalendar,
  Edit as IconEdit,
  DataLine as IconDataLine,
  Setting as IconSetting,
} from '@element-plus/icons-vue';

const route = useRoute();
const activeRoute = computed(() => route.path);
</script>

<style scoped>
.main-layout {
  height: 100vh;
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  border-bottom: 1px solid var(--el-border-color);
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.logo-container span {
  font-size: 18px;
  font-weight: bold;
}

.el-menu {
  border-right: none;
}
</style>
