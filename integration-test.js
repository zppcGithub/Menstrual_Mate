const axios = require('axios')

const BASE_URL = 'http://localhost:8080'

async function testHealth() {
  try {
    const response = await axios.get(`${BASE_URL}/actuator/health`)
    console.log('✅ 健康检查通过:', response.data)
    return true
  } catch (error) {
    console.error('❌ 健康检查失败:', error.message)
    return false
  }
}

async function testCycleAPI() {
  try {
    // 测试创建周期记录
    const cycleData = {
      startDate: '2024-01-01',
      endDate: '2024-01-05',
      cycleLength: 28,
      periodLength: 5
    }
    
    const response = await axios.post(`${BASE_URL}/api/cycles`, cycleData)
    console.log('✅ 创建周期记录成功:', response.data)
    
    // 测试获取周期列表
    const listResponse = await axios.get(`${BASE_URL}/api/cycles`)
    console.log('✅ 获取周期列表成功:', listResponse.data.data.length, '条记录')
    
    return true
  } catch (error) {
    console.error('❌ 周期API测试失败:', error.response?.data || error.message)
    return false
  }
}

async function testDailyRecordAPI() {
  try {
    // 测试创建每日记录
    const dailyData = {
      recordDate: '2024-01-01',
      menstrualFlow: 'MEDIUM',
      temperature: 36.5,
      weight: 55.5,
      symptoms: ['腹痛', '疲劳'],
      notes: '今日感觉良好'
    }
    
    const response = await axios.post(`${BASE_URL}/api/daily-records`, dailyData)
    console.log('✅ 创建每日记录成功:', response.data)
    
    // 测试获取每日记录
    const listResponse = await axios.get(`${BASE_URL}/api/daily-records?startDate=2024-01-01&endDate=2024-01-31`)
    console.log('✅ 获取每日记录成功:', listResponse.data.data.length, '条记录')
    
    return true
  } catch (error) {
    console.error('❌ 每日记录API测试失败:', error.response?.data || error.message)
    return false
  }
}

async function testCalendarView() {
  try {
    const response = await axios.get(`${BASE_URL}/api/calendar-view?year=2024&month=1`)
    console.log('✅ 日历视图API测试成功:', response.data.data.length, '天数据')
    return true
  } catch (error) {
    console.error('❌ 日历视图API测试失败:', error.response?.data || error.message)
    return false
  }
}

async function runIntegrationTests() {
  console.log('🚀 开始集成测试...\n')
  
  let passed = 0
  let total = 4
  
  console.log('1. 健康检查...')
  if (await testHealth()) passed++
  
  console.log('\n2. 周期API测试...')
  if (await testCycleAPI()) passed++
  
  console.log('\n3. 每日记录API测试...')
  if (await testDailyRecordAPI()) passed++
  
  console.log('\n4. 日历视图API测试...')
  if (await testCalendarView()) passed++
  
  console.log(`\n📊 测试结果: ${passed}/${total} 测试通过`)
  
  if (passed === total) {
    console.log('🎉 所有集成测试通过！')
  } else {
    console.log('⚠️  部分测试失败，请检查服务状态')
  }
}

// 如果直接运行此文件
if (require.main === module) {
  runIntegrationTests().catch(console.error)
}

module.exports = { runIntegrationTests }