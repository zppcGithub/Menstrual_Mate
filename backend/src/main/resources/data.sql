-- 健康提示表初始化数据
INSERT INTO health_tips (id, title, content, type, phase, created_at) VALUES
(1, '经期注意事项', '经期期间避免剧烈运动，多喝温水，注意保暖', 'REMINDER', 'MENSTRUATION', NOW()),
(2, '排卵期健康', '排卵期注意休息，避免过度劳累，保持良好心情', 'HEALTH', 'OVULATION', NOW()),
(3, '经前综合症缓解', '经前综合症可以通过适当运动和饮食调节来缓解', 'HEALTH', 'PRE_MENSTRUATION', NOW()),
(4, '营养补充建议', '经期后注意补充铁质和蛋白质，多吃绿叶蔬菜', 'NUTRITION', 'POST_MENSTRUATION', NOW()),
(5, '运动建议', '经期后一周是运动的最佳时期，可以适当增加运动量', 'EXERCISE', 'FOLLICULAR', NOW()),
(6, '情绪管理', '记录情绪波动，学会放松和调节心情', 'MENTAL', 'LUTEAL', NOW()),
(7, '睡眠质量', '保持规律作息，提高睡眠质量有助于调节内分泌', 'LIFESTYLE', 'ALL_PHASES', NOW()),
(8, '水分摄入', '每天保持充足的水分摄入，有助于新陈代谢', 'NUTRITION', 'ALL_PHASES', NOW()),
(9, '压力管理', '学会管理压力，避免过度紧张和焦虑', 'MENTAL', 'ALL_PHASES', NOW()),
(10, '定期体检', '建议每年进行一次妇科检查，关注身体健康', 'HEALTH', 'ALL_PHASES', NOW());

-- 症状标签表初始化数据
INSERT INTO symptoms (id, name, type, severity_level, created_at) VALUES
(1, '腹痛', 'PHYSICAL', 2, NOW()),
(2, '腰痛', 'PHYSICAL', 2, NOW()),
(3, '头痛', 'PHYSICAL', 1, NOW()),
(4, '乳房胀痛', 'PHYSICAL', 1, NOW()),
(5, '情绪波动', 'EMOTIONAL', 2, NOW()),
(6, '易怒', 'EMOTIONAL', 2, NOW()),
(7, '疲劳', 'PHYSICAL', 1, NOW()),
(8, '失眠', 'PHYSICAL', 2, NOW()),
(9, '食欲变化', 'PHYSICAL', 1, NOW()),
(10, '皮肤变化', 'PHYSICAL', 1, NOW()),
(11, '腹胀', 'PHYSICAL', 1, NOW()),
(12, '恶心', 'PHYSICAL', 2, NOW()),
(13, '便秘', 'PHYSICAL', 1, NOW()),
(14, '腹泻', 'PHYSICAL', 1, NOW()),
(15, '水肿', 'PHYSICAL', 1, NOW());

-- 系统设置表初始化数据
INSERT INTO system_settings (id, setting_key, setting_value, description, created_at) VALUES
(1, 'app.name', '经期伴侣', '应用名称', NOW()),
(2, 'app.version', '1.0.0', '应用版本', NOW()),
(3, 'prediction.enabled', 'true', '是否启用周期预测功能', NOW()),
(4, 'notification.enabled', 'true', '是否启用通知提醒', NOW()),
(5, 'backup.auto', 'true', '是否启用自动备份', NOW());