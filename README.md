# 足球联赛数据管理系统

## 项目概述

足球联赛数据管理系统是一个基于前后端分离架构的Web应用，用于管理足球联赛的球员、球队、联赛、比赛和比赛记录等数据，并提供球员雷达图分析功能。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.14
- **ORM**: MyBatis Plus 3.5.3
- **数据库**: MySQL 8.0.33
- **开发语言**: Java 1.8

### 前端
- **框架**: Vue 2.6.14
- **UI组件库**: Element UI 2.15.13
- **图表库**: ECharts 6.0.0
- **路由**: Vue Router 3.5.2
- **HTTP客户端**: Axios 0.27.2

## 功能模块

1. **用户管理**
   - 用户注册、登录
   - 权限管理（游客、赛事组织者、球队工作人员）

2. **球员管理**
   - 球员信息增删改查
   - 球员雷达图分析（多维度对比）

3. **球队管理**
   - 球队信息增删改查

4. **联赛管理**
   - 联赛信息增删改查

5. **比赛管理**
   - 比赛信息增删改查

6. **比赛记录管理**
   - 比赛记录增删改查

## 环境要求

### 后端
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+

### 前端
- Node.js 14+
- npm 6+

## 安装与运行

### 1. 数据库准备

1. 创建数据库：
```sql
CREATE DATABASE football_league;
```

2. 执行初始化SQL脚本：
```bash
mysql -u root -p football_league < backend/src/main/resources/sql/init.sql
```

### 2. 后端配置与运行

1. 修改数据库配置（backend/src/main/resources/application.yml）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/football_league?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
```

2. 运行后端服务：
```bash
cd backend
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

### 3. 前端配置与运行

1. 安装依赖：
```bash
cd frontend
npm install
```

2. 运行前端服务：
```bash
npm run serve
```

前端服务将在 `http://localhost:3000` 启动。

## 项目结构

```
.
├── backend/                  # 后端项目
│   ├── src/main/java/com/football/  # 后端源码
│   │   ├── common/          # 公共组件
│   │   ├── config/          # 配置类
│   │   ├── controller/      # 控制器
│   │   ├── entity/          # 实体类
│   │   ├── mapper/          # 数据访问层
│   │   ├── service/         # 业务逻辑层
│   │   └── FootballLeagueApplication.java  # 启动类
│   ├── src/main/resources/  # 资源文件
│   │   ├── sql/             # SQL脚本
│   │   └── application.yml  # 应用配置
│   └── pom.xml              # Maven配置
├── frontend/                 # 前端项目
│   ├── public/              # 公共资源
│   ├── src/                 # 前端源码
│   │   ├── assets/          # 静态资源
│   │   ├── layout/          # 布局组件
│   │   ├── router/          # 路由配置
│   │   ├── views/           # 页面组件
│   │   ├── App.vue          # 根组件
│   │   └── main.js          # 入口文件
│   ├── package.json         # npm配置
│   └── vue.config.js        # Vue配置
└── README.md                # 项目说明
```

## 主要功能说明

### 球员雷达图分析

球员雷达图分析功能允许用户选择多个球员（至少2名）和多个分析维度，生成雷达图进行多维度对比分析。

**支持的分析维度**：
- 进球数
- 助攻数
- 出场数
- 年龄
- 身高
- 体重
- 球衣号码

**使用方法**：
1. 进入球员管理页面
2. 点击右下角"雷达图分析"按钮
3. 选择至少两名球员
4. 选择分析维度
5. 点击"生成雷达图"按钮查看分析结果

## 数据库设计

系统包含以下主要表：

- **user**：用户信息表
- **player**：球员信息表
- **team**：球队信息表
- **league**：联赛信息表
- **match**：比赛信息表
- **match_record**：比赛记录表

详细表结构请参考 `backend/src/main/resources/sql/init.sql` 文件。

## 权限说明

系统分为三种角色：

1. **游客**：只能查看数据，不能进行修改操作
2. **赛事组织者**：可以对所有数据进行增删改查操作
3. **球队工作人员**：只能对自己球队的球员数据进行修改操作

## 注意事项

1. 首次运行时请确保数据库已创建并执行了初始化脚本
2. 请根据实际情况修改数据库配置
3. 前端服务默认端口为3000，后端服务默认端口为8080
4. 球员雷达图分析功能需要选择至少两名球员和一个分析维度

## 联系方式

如有问题或建议，请联系系统管理员。