# 数据库配置说明

## 数据库结构

### 核心表结构

1. **users** - 用户表（支持未来多用户扩展）
2. **cycle_records** - 周期记录表（核心）
3. **daily_records** - 每日详情记录表
4. **symptoms** - 症状标签字典表
5. **symptom_records** - 症状关联表（多对多）
6. **health_tips** - 健康提示表
7. **system_settings** - 系统设置表

### 数据字典

#### 月经流量 (menstrual_flow)
- SPOTTING: 点滴
- LIGHT: 少量
- MEDIUM: 中等
- HEAVY: 大量

#### 症状类型 (type)
- SYMPTOM: 症状
- MOOD: 情绪
- ACTIVITY: 活动

#### 周期阶段 (phase)
- MENSTRUAL: 月经期
- FOLLICULAR: 卵泡期
- OVULATION: 排卵期
- LUTEAL: 黄体期

## 安装步骤

### 1. MySQL安装配置
```bash
# 安装MySQL 8.0
# 创建数据库
mysql -u root -p
source database-schema.sql
```

### 2. 连接配置
```properties
# application.yml配置示例
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/menstrual_mate?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

### 3. 测试数据
数据库初始化脚本已包含：
- 默认症状标签（15个）
- 健康提示（10条）
- 默认用户配置
- 系统设置默认值

## 性能优化

### 索引设计
- 用户ID+日期组合索引优化日历查询
- 症状类型索引优化标签筛选
- 日期范围索引优化周期统计

### 视图说明
- v_cycle_summary: 周期汇总视图，包含计算字段