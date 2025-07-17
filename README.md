# 经期伴侣 (Menstrual Mate) 开发完成指南

## 🎉 项目已完成！

### 项目概述
现代化生理周期跟踪应用，采用Spring Boot + Vue.js + Electron技术栈，支持跨平台桌面应用。

### 技术栈
- **后端**: Spring Boot 3.2.0 + Java 17 + MySQL 8.0
- **前端**: Vue 3 + TypeScript + Element Plus + ECharts
- **桌面**: Electron + Node.js
- **容器**: Docker + Docker Compose

### 快速开始

#### 开发环境启动
```bash
# 方式1：使用一键启动脚本（Windows）
double-click run-dev.bat

# 方式2：手动启动
cd backend && mvn spring-boot:run  # 启动后端
cd frontend && npm run dev        # 启动前端
```

#### 生产环境部署
```bash
# Docker容器化部署
docker-compose up -d

# 访问地址
前端: http://localhost:3000
后端: http://localhost:8080
Swagger: http://localhost:8080/swagger-ui.html
```

#### Electron桌面应用打包
```bash
# 安装依赖
npm install

# 开发模式
electron electron/main.js

# 打包应用
npm run build:win    # Windows
npm run build:mac    # macOS  
npm run build:linux  # Linux
```

### 功能特性

#### ✅ 核心功能
- **周期记录**: 完整的经期周期跟踪
- **每日记录**: 症状、体温、体重等详细信息记录
- **日历视图**: 可视化经期流量和症状
- **数据分析**: ECharts图表展示周期趋势
- **主题切换**: 支持深色/浅色主题
- **数据管理**: JSON/CSV导入导出、备份恢复

#### ✅ 技术特性
- **响应式设计**: 完美适配桌面和移动设备
- **RESTful API**: 统一的API设计规范
- **实时验证**: 前端表单验证 + 后端数据验证
- **错误处理**: 完善的异常处理和用户提示
- **跨平台**: Windows/macOS/Linux原生应用支持

### 项目结构
```
menstrual-mate/
├── backend/                 # Spring Boot后端
│   ├── src/main/java/com/menstrualmate/
│   │   ├── controller/      # REST API控制器
│   │   ├── service/         # 业务逻辑层
│   │   ├── repository/      # 数据访问层
│   │   ├── entity/          # 数据库实体
│   │   └── dto/             # 数据传输对象
│   └── Dockerfile           # 后端容器配置
├── frontend/                # Vue 3前端
│   ├── src/
│   │   ├── views/           # 页面组件
│   │   ├── stores/          # Pinia状态管理
│   │   ├── types/           # TypeScript类型定义
│   │   └── utils/           # 工具函数
│   ├── Dockerfile           # 前端容器配置
│   └── nginx.conf           # Nginx配置
├── electron/                # Electron桌面应用
│   ├── main.js              # 主进程
│   └── preload.js           # 预加载脚本
├── database-schema.sql      # 数据库初始化脚本
├── docker-compose.yml       # 容器编排配置
└── package.json             # 项目配置
```

### API端点

#### 周期记录管理
- `GET /api/cycles` - 获取所有周期记录
- `POST /api/cycles` - 创建新的周期记录
- `PUT /api/cycles/{id}` - 更新周期记录
- `DELETE /api/cycles/{id}` - 删除周期记录

#### 每日记录管理
- `GET /api/daily-records` - 获取每日记录
- `POST /api/daily-records` - 创建每日记录
- `PUT /api/daily-records/{id}` - 更新每日记录
- `DELETE /api/daily-records/{id}` - 删除每日记录

#### 日历视图
- `GET /api/calendar-view` - 获取日历视图数据

#### 健康提示
- `GET /api/tips` - 获取健康提示
- `GET /api/tips/random` - 获取随机健康提示

### 测试

#### 后端测试
```bash
cd backend
mvn test                    # 运行单元测试
mvn integration-test        # 运行集成测试
```

#### 前端测试
```bash
cd frontend
npm run test               # 运行测试
npm run test:unit          # 运行单元测试
```

#### 集成测试
```bash
node integration-test.js   # 运行前后端集成测试
```

### 环境变量

#### 后端环境变量
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/menstrual_mate
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root123
```

#### 前端环境变量
```bash
VITE_API_BASE_URL=http://localhost:8080
```

### 数据库配置
- **数据库**: MySQL 8.0
- **端口**: 3306
- **用户名**: root / menstrual_user
- **密码**: root123 / menstrual_pass

### 部署指南

#### 1. Docker部署
```bash
# 克隆项目
git clone https://github.com/zppcGithub/Menstrual_Mate.git
cd Menstrual_Mate

# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

#### 2. 手动部署
```bash
# 1. 启动MySQL
docker run -d --name mysql \
  -e MYSQL_ROOT_PASSWORD=root123 \
  -e MYSQL_DATABASE=menstrual_mate \
  -p 3306:3306 mysql:8.0

# 2. 启动后端
cd backend
mvn spring-boot:run

# 3. 启动前端
cd frontend
npm install
npm run dev
```

### 常见问题

#### Q: 前端无法连接后端
A: 检查前端`vite.config.ts`中的代理配置，确保后端服务运行在8080端口

#### Q: MySQL连接失败
A: 确保MySQL已启动，检查数据库配置和连接参数

#### Q: Electron应用打包失败
A: 确保已安装所有依赖，使用管理员权限运行打包命令

### 开发团队
- **项目**: 经期伴侣 (Menstrual Mate)
- **版本**: 1.0.0
- **技术栈**: Spring Boot + Vue.js + Electron
- **许可证**: MIT

### 支持与反馈
如有问题或建议，请通过以下方式联系：
- GitHub Issues: https://github.com/zppcGithub/Menstrual_Mate/issues
- 邮箱: your-email@example.com

---

**开发完成时间**: 2025-07-17
**项目状态**: ✅ 已部署完成，可正常使用