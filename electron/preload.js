const { contextBridge, ipcRenderer } = require('electron')

// 安全的API暴露
contextBridge.exposeInMainWorld('electronAPI', {
  // 应用信息
  getAppVersion: () => ipcRenderer.invoke('get-app-version'),
  
  // 文件操作
  showSaveDialog: () => ipcRenderer.invoke('show-save-dialog'),
  showOpenDialog: () => ipcRenderer.invoke('show-open-dialog'),
  writeFile: (filePath, data) => ipcRenderer.invoke('write-file', filePath, data),
  readFile: (filePath) => ipcRenderer.invoke('read-file', filePath),
  
  // 导航
  navigateTo: (route) => ipcRenderer.send('navigate-to', route),
  
  // 监听事件
  onNavigate: (callback) => {
    ipcRenderer.on('navigate-to', (event, route) => callback(route))
  },
  
  // 移除监听器
  removeAllListeners: (channel) => {
    ipcRenderer.removeAllListeners(channel)
  }
})