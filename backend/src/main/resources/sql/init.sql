-- 创建数据库
CREATE DATABASE IF NOT EXISTS football_league DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE football_league;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `phone` VARCHAR(20) COMMENT '手机号',
  `role` INT DEFAULT 0 COMMENT '角色 0-游客 1-赛事组织者 2-球队工作人员',
  `team_id` INT COMMENT '所属球队ID（仅球队工作人员需要）',
  `status` INT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 联赛表
CREATE TABLE IF NOT EXISTS `league` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '联赛ID',
  `name` VARCHAR(100) NOT NULL COMMENT '联赛名称',
  `season` VARCHAR(20) NOT NULL COMMENT '赛季',
  `start_date` DATE COMMENT '开始日期',
  `end_date` DATE COMMENT '结束日期',
  `status` INT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联赛表';

-- 球队表
CREATE TABLE IF NOT EXISTS `team` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '球队ID',
  `name` VARCHAR(100) NOT NULL COMMENT '球队名称',
  `city` VARCHAR(50) COMMENT '所在城市',
  `coach` VARCHAR(50) COMMENT '主教练',
  `home_stadium` VARCHAR(100) COMMENT '主场',
  `founded_year` INT COMMENT '成立年份',
  `status` INT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='球队表';

-- 球员表
CREATE TABLE IF NOT EXISTS `player` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '球员ID',
  `team_id` INT NOT NULL COMMENT '球队ID',
  `name` VARCHAR(50) NOT NULL COMMENT '球员姓名',
  `number` INT COMMENT '球衣号码',
  `position` VARCHAR(20) COMMENT '位置',
  `age` INT COMMENT '年龄',
  `nationality` VARCHAR(50) COMMENT '国籍',
  `height` DECIMAL(5,2) COMMENT '身高(米)',
  `weight` DECIMAL(5,2) COMMENT '体重(公斤)',
  `dominant_foot` VARCHAR(10) COMMENT '惯用脚 左/右',
  `goals` INT DEFAULT 0 COMMENT '进球数',
  `assists` INT DEFAULT 0 COMMENT '助攻数',
  `appearances` INT DEFAULT 0 COMMENT '出场数',
  `status` INT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='球员表';

-- 比赛表
CREATE TABLE IF NOT EXISTS `match` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '比赛ID',
  `league_id` INT NOT NULL COMMENT '联赛ID',
  `home_team_id` INT NOT NULL COMMENT '主队ID',
  `away_team_id` INT NOT NULL COMMENT '客队ID',
  `match_date` DATETIME NOT NULL COMMENT '比赛时间',
  `home_score` INT DEFAULT 0 COMMENT '主队得分',
  `away_score` INT DEFAULT 0 COMMENT '客队得分',
  `stadium` VARCHAR(100) COMMENT '比赛场地',
  `status` INT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='比赛表';

-- 比赛记录表（进球、红黄牌等）
CREATE TABLE IF NOT EXISTS `match_record` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `match_id` INT NOT NULL COMMENT '比赛ID',
  `player_id` INT NOT NULL COMMENT '球员ID',
  `event_type` INT NOT NULL COMMENT '事件类型 1-进球 2-黄牌 3-红牌 4-助攻',
  `event_time` INT COMMENT '事件发生时间(分钟)',
  `description` VARCHAR(200) COMMENT '描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='比赛记录表';

-- 插入测试数据

-- 用户数据
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `role`, `team_id`, `status`) VALUES
('admin', '123456', '管理员', '13800138000', 1, NULL, 1),
('organizer', '123456', '赛事组织者', '13800138001', 1, NULL, 1),
('teamstaff1', '123456', '球队工作人员1', '13800138002', 2, 1, 1),
('teamstaff2', '123456', '球队工作人员2', '13800138003', 2, 2, 1),
('guest', '123456', '游客', '13800138004', 0, NULL, 1);

-- 联赛数据
INSERT INTO `league` (`name`, `season`, `start_date`, `end_date`, `status`) VALUES
('中超联赛', '2025赛季', '2025-03-01', '2025-11-30', 1),
('英超联赛', '2025赛季', '2025-08-10', '2025-05-25', 1),
('西甲联赛', '2025赛季', '2025-08-17', '2025-05-30', 1);

-- 球队数据
INSERT INTO `team` (`name`, `city`, `coach`, `home_stadium`, `founded_year`, `status`) VALUES
('北京国安', '北京', '谢峰', '北京工人体育场', 1992, 1),
('上海申花', '上海', '吴金贵', '虹口足球场', 1993, 1),
('广州恒大', '广州', '卡纳瓦罗', '天河体育场', 2010, 1),
('曼城', '曼彻斯特', '瓜迪奥拉', '伊蒂哈德球场', 1880, 1),
('利物浦', '利物浦', '克洛普', '安菲尔德球场', 1892, 1),
('皇马', '马德里', '安切洛蒂', '伯纳乌球场', 1902, 1),
('巴萨', '巴塞罗那', '哈维', '诺坎普球场', 1899, 1);

-- 球员数据
INSERT INTO `player` (`team_id`, `name`, `number`, `position`, `age`, `nationality`, `height`, `weight`, `dominant_foot`, `goals`, `assists`, `appearances`, `status`) VALUES
(1, '张玉宁', 9, '前锋', 26, '中国', 1.85, 80, '右', 15, 5, 20, 1),
(1, '李磊', 4, '后卫', 29, '中国', 1.75, 70, '左', 2, 8, 25, 1),
(1, '王子铭', 20, '中场', 24, '中国', 1.78, 72, '右', 8, 12, 22, 1),
(2, '武磊', 7, '前锋', 32, '中国', 1.72, 66, '右', 18, 6, 24, 1),
(2, '曹赟定', 28, '中场', 34, '中国', 1.75, 68, '左', 5, 15, 26, 1),
(3, '保利尼奥', 8, '中场', 35, '巴西', 1.82, 78, '右', 10, 18, 28, 1),
(3, '塔利斯卡', 9, '前锋', 29, '巴西', 1.83, 76, '左', 22, 4, 20, 1),
(4, '哈兰德', 9, '前锋', 24, '挪威', 1.94, 88, '左', 35, 8, 30, 1),
(4, '德布劳内', 17, '中场', 32, '比利时', 1.81, 70, '右', 6, 25, 28, 1),
(5, '萨拉赫', 11, '前锋', 31, '埃及', 1.75, 71, '左', 28, 10, 32, 1),
(5, '范迪克', 4, '后卫', 32, '荷兰', 1.93, 92, '右', 3, 2, 30, 1),
(6, '本泽马', 9, '前锋', 36, '法国', 1.85, 81, '右', 30, 12, 35, 1),
(6, '莫德里奇', 10, '中场', 38, '克罗地亚', 1.72, 68, '左', 4, 20, 33, 1),
(7, '莱万多夫斯基', 9, '前锋', 35, '波兰', 1.85, 80, '右', 32, 7, 31, 1),
(7, '佩德里', 8, '中场', 21, '西班牙', 1.74, 68, '左', 5, 18, 26, 1);

-- 比赛数据
INSERT INTO `match` (`league_id`, `home_team_id`, `away_team_id`, `match_date`, `home_score`, `away_score`, `stadium`, `status`) VALUES
(1, 1, 2, '2025-03-15 19:30:00', 2, 1, '北京工人体育场', 1),
(1, 2, 3, '2025-03-20 19:35:00', 3, 2, '虹口足球场', 1),
(1, 3, 1, '2025-03-25 20:00:00', 1, 1, '天河体育场', 1),
(2, 4, 5, '2025-08-17 19:00:00', 2, 2, '伊蒂哈德球场', 1),
(2, 5, 4, '2025-08-24 16:30:00', 1, 3, '安菲尔德球场', 1),
(3, 6, 7, '2025-08-20 20:00:00', 3, 1, '伯纳乌球场', 1),
(3, 7, 6, '2025-08-27 22:00:00', 2, 2, '诺坎普球场', 1);

-- 比赛记录数据
INSERT INTO `match_record` (`match_id`, `player_id`, `event_type`, `event_time`, `description`) VALUES
(1, 1, 1, 15, '张玉宁进球'),
(1, 4, 1, 23, '武磊进球'),
(1, 1, 1, 45, '张玉宁梅开二度'),
(1, 28, 2, 55, '曹赟定犯规黄牌'),
(2, 4, 1, 8, '武磊首开纪录'),
(2, 9, 1, 25, '塔利斯卡扳平'),
(2, 28, 1, 40, '曹赟定破门'),
(2, 9, 1, 62, '塔利斯卡梅开二度'),
(2, 4, 1, 78, '武磊再度破门'),
(4, 9, 1, 23, '哈兰德破门'),
(4, 11, 1, 45, '萨拉赫扳平'),
(4, 9, 1, 67, '哈兰德梅开二度'),
(4, 11, 1, 89, '萨拉赫绝平'),
(6, 9, 1, 18, '本泽马首开纪录'),
(6, 9, 1, 42, '莱万多夫斯基扳平'),
(6, 10, 1, 56, '莫德里奇破门'),
(6, 8, 1, 67, '佩德里扳回一球'),
(6, 9, 1, 78, '本泽马梅开二度');

