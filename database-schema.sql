-- 经期伴侣 (Menstrual Mate) 数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS menstrual_mate CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE menstrual_mate;

-- 用户表 (为未来多用户扩展预留)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- 周期记录表
CREATE TABLE IF NOT EXISTS cycle_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT DEFAULT 1, -- 默认为单用户
    start_date DATE NOT NULL,
    end_date DATE,
    cycle_length INT, -- 本次周期长度（天）
    period_length INT, -- 经期长度（天）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_start (user_id, start_date),
    INDEX idx_start_date (start_date)
);

-- 每日详情记录表
CREATE TABLE IF NOT EXISTS daily_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_date DATE UNIQUE NOT NULL,
    menstrual_flow VARCHAR(20), -- 经血流量: 'SPOTTING', 'LIGHT', 'MEDIUM', 'HEAVY'
    temperature DECIMAL(4,1), -- 基础体温
    weight DECIMAL(5,2), -- 体重
    notes TEXT, -- 文字备注
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_record_date (record_date)
);

-- 症状标签字典表
CREATE TABLE IF NOT EXISTS symptoms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL, -- 标签类型: 'SYMPTOM', 'MOOD', 'ACTIVITY'
    icon VARCHAR(50), -- 图标标识
    color VARCHAR(7) DEFAULT '#666666', -- 颜色代码
    is_custom BOOLEAN DEFAULT FALSE,
    user_id BIGINT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_symptom (name, type, user_id)
);

-- 症状关联表 (多对多关系)
CREATE TABLE IF NOT EXISTS symptom_records (
    daily_record_id BIGINT NOT NULL,
    symptom_id BIGINT NOT NULL,
    intensity INT DEFAULT 1, -- 强度: 1-5
    PRIMARY KEY (daily_record_id, symptom_id),
    FOREIGN KEY (daily_record_id) REFERENCES daily_records(id) ON DELETE CASCADE,
    FOREIGN KEY (symptom_id) REFERENCES symptoms(id) ON DELETE CASCADE
);

-- 健康提示表
CREATE TABLE IF NOT EXISTS health_tips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phase VARCHAR(20) NOT NULL, -- 周期阶段: 'MENSTRUAL', 'FOLLICULAR', 'OVULATION', 'LUTEAL'
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(20), -- 类别: 'HEALTH', 'DIET', 'EXERCISE', 'SKINCARE'
    priority INT DEFAULT 1, -- 优先级: 1-5
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 系统设置表
CREATE TABLE IF NOT EXISTS system_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT DEFAULT 1,
    setting_key VARCHAR(50) NOT NULL,
    setting_value TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_setting (user_id, setting_key),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 插入默认症状标签
INSERT INTO symptoms (name, type, icon, color, is_custom) VALUES
-- 症状类
('腹痛', 'SYMPTOM', 'stomach-ache', '#FF6B6B', FALSE),
('乳房胀痛', 'SYMPTOM', 'breast-pain', '#FF6B6B', FALSE),
('头痛', 'SYMPTOM', 'headache', '#FF6B6B', FALSE),
('腰酸', 'SYMPTOM', 'back-pain', '#FF6B6B', FALSE),
('恶心', 'SYMPTOM', 'nausea', '#FF6B6B', FALSE),
('疲劳', 'SYMPTOM', 'fatigue', '#FF6B6B', FALSE),

-- 情绪类
('愉悦', 'MOOD', 'happy', '#4ECDC4', FALSE),
('平静', 'MOOD', 'calm', '#4ECDC4', FALSE),
('易怒', 'MOOD', 'angry', '#FF7675', FALSE),
('焦虑', 'MOOD', 'anxious', '#FF7675', FALSE),
('敏感', 'MOOD', 'sensitive', '#FF7675', FALSE),

-- 活动类
('运动', 'ACTIVITY', 'exercise', '#95E1D3', FALSE),
('同房', 'ACTIVITY', 'intimacy', '#F8B500', FALSE),
('熬夜', 'ACTIVITY', 'late-night', '#A8E6CF', FALSE),
('饮酒', 'ACTIVITY', 'alcohol', '#FFD93D', FALSE);

-- 插入默认健康提示
INSERT INTO health_tips (phase, title, content, category, priority) VALUES
('MENSTRUAL', '经期保暖', '注意腹部保暖，可以喝些温热的红糖水或姜茶缓解痛经。', 'HEALTH', 5),
('MENSTRUAL', '温和运动', '适量做些轻度运动如散步或瑜伽，有助于缓解经期不适。', 'EXERCISE', 4),
('MENSTRUAL', '营养补充', '多食用富含铁质的食物如瘦肉、菠菜，补充因月经流失的铁元素。', 'DIET', 5),

('FOLLICULAR', '精力充沛', '这是精力最充沛的时期，适合安排重要工作或高强度运动。', 'HEALTH', 4),
('FOLLICULAR', '皮肤状态佳', '雌激素水平上升，皮肤状态最佳，是进行美容护理的好时机。', 'SKINCARE', 3),

('OVULATION', '易受孕提醒', '排卵期受孕几率最高，如需避孕请特别注意防护措施。', 'HEALTH', 5),
('OVULATION', '轻微腹痛', '部分女性会感到轻微腹痛（排卵痛），这是正常现象。', 'HEALTH', 3),

('LUTEAL', '情绪波动', '孕激素水平升高可能导致情绪波动，建议保持心情愉悦，避免压力过大。', 'HEALTH', 4),
('LUTEAL', '减少盐分', '为预防经前水肿，建议减少盐分摄入，多喝水。', 'DIET', 3),
('LUTEAL', '皮肤护理', '激素变化可能导致皮肤出油或长痘，注意清洁和保湿。', 'SKINCARE', 4);

-- 插入默认用户（单用户模式）
INSERT INTO users (username, email) VALUES ('default_user', 'user@example.com');

-- 插入默认系统设置
INSERT INTO system_settings (user_id, setting_key, setting_value) VALUES
(1, 'theme', 'pink'),
(1, 'language', 'zh-CN'),
(1, 'backup_enabled', 'true'),
(1, 'notifications_enabled', 'true'),
(1, 'cycle_length_avg', '28'),
(1, 'period_length_avg', '5');

-- 创建视图：简化查询
CREATE VIEW v_cycle_summary AS
SELECT 
    cr.id,
    cr.start_date,
    cr.end_date,
    cr.cycle_length,
    cr.period_length,
    DATEDIFF(COALESCE(cr.end_date, CURDATE()), cr.start_date) + 1 as actual_period_length,
    LAG(cr.start_date) OVER (ORDER BY cr.start_date) as prev_start_date,
    DATEDIFF(cr.start_date, LAG(cr.start_date) OVER (ORDER BY cr.start_date)) as actual_cycle_length
FROM cycle_records cr
ORDER BY cr.start_date DESC;

-- 创建索引优化查询性能
CREATE INDEX idx_cycle_user_date ON cycle_records(user_id, start_date);
CREATE INDEX idx_daily_date ON daily_records(record_date);
CREATE INDEX idx_symptom_type ON symptoms(type, user_id);