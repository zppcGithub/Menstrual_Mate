# 经期伴侣 API 测试文档

## 测试环境
- **基础URL**: http://localhost:8080
- **Swagger文档**: http://localhost:8080/swagger-ui.html

## 核心API端点测试

### 1. 周期记录 API (/api/cycles)

#### 获取所有周期记录
```bash
curl -X GET http://localhost:8080/api/cycles
```

#### 创建周期记录
```bash
curl -X POST http://localhost:8080/api/cycles \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2024-01-01",
    "endDate": "2024-01-05",
    "cycleLength": 28,
    "periodLength": 5
  }'
```

#### 获取特定周期记录
```bash
curl -X GET http://localhost:8080/api/cycles/1
```

#### 更新周期记录
```bash
curl -X PUT http://localhost:8080/api/cycles/1 \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2024-02-01",
    "endDate": "2024-02-06",
    "cycleLength": 30,
    "periodLength": 6
  }'
```

#### 删除周期记录
```bash
curl -X DELETE http://localhost:8080/api/cycles/1
```

#### 获取最新周期记录
```bash
curl -X GET http://localhost:8080/api/cycles/latest
```

#### 预测下次周期
```bash
curl -X GET http://localhost:8080/api/cycles/predict
```

### 2. 每日记录 API (/api/daily-records)

#### 获取所有每日记录
```bash
curl -X GET http://localhost:8080/api/daily-records
```

#### 创建每日记录
```bash
curl -X POST http://localhost:8080/api/daily-records \
  -H "Content-Type: application/json" \
  -d '{
    "recordDate": "2024-01-03",
    "menstrualFlow": "MEDIUM",
    "temperature": 36.5,
    "weight": 55.2,
    "notes": "今日感觉良好"
  }'
```

#### 获取特定日期记录
```bash
curl -X GET http://localhost:8080/api/daily-records/date/2024-01-03
```

### 3. 日历视图 API (/api/calendar)

#### 获取日历视图
```bash
curl -X GET "http://localhost:8080/api/calendar/view?yearMonth=2024-01-15"
```

#### 获取每日事件
```bash
curl -X GET "http://localhost:8080/api/calendar/events/2024-01-15"
```

#### 获取分析数据
```bash
curl -X GET "http://localhost:8080/api/calendar/analysis?startDate=2024-01-01&endDate=2024-12-31"
```

### 4. 健康提示 API (/api/tips)

#### 获取所有健康提示
```bash
curl -X GET http://localhost:8080/api/tips
```

#### 按阶段获取健康提示
```bash
curl -X GET http://localhost:8080/api/tips/phase/MENSTRUAL
```

#### 按类别获取健康提示
```bash
curl -X GET http://localhost:8080/api/tips/category/HEALTH
```

## 测试用例

### 测试场景1：完整周期管理流程
1. 创建新的周期记录
2. 添加每日记录
3. 查看日历视图
4. 获取健康提示
5. 查看分析数据

### 测试场景2：数据验证
1. 测试无效的日期格式
2. 测试必填字段缺失
3. 测试数值范围验证

### 测试场景3：异常处理
1. 测试不存在的记录ID
2. 测试无效的枚举值
3. 测试日期范围错误

## 响应格式示例

### 成功响应
```json
{
  "success": true,
  "message": "操作成功",
  "data": { ... }
}
```

### 错误响应
```json
{
  "success": false,
  "message": "错误信息",
  "data": null
}
```

## 运行测试

### 启动测试环境
```bash
# 启动Docker环境
docker-compose up -d

# 等待MySQL启动完成
sleep 30

# 检查服务状态
curl -X GET http://localhost:8080/api/cycles
```

### 运行自动化测试
```bash
cd backend
mvn test
```

## 测试数据

### 初始化测试数据
```sql
-- 插入测试周期记录
INSERT INTO cycle_records (user_id, start_date, end_date, cycle_length, period_length) 
VALUES (1, '2024-01-01', '2024-01-05', 28, 5);

-- 插入测试每日记录
INSERT INTO daily_records (record_date, menstrual_flow, temperature, weight, notes) 
VALUES 
('2024-01-01', 'LIGHT', 36.5, 55.0, '第一天，感觉还好'),
('2024-01-02', 'MEDIUM', 36.6, 55.1, '第二天，轻微腹痛'),
('2024-01-03', 'HEAVY', 36.4, 55.2, '第三天，流量较多');
```

## 性能测试

### 并发测试
```bash
# 使用Apache Bench进行并发测试
ab -n 1000 -c 10 http://localhost:8080/api/cycles
```

### 负载测试
```bash
# 使用JMeter或Postman进行负载测试
# 建议测试场景：
# 1. 100个并发用户
# 2. 1000个请求
# 3. 平均响应时间 < 500ms
```