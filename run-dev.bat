@echo off
echo 正在启动经期伴侣开发环境...

REM 启动MySQL容器（如果尚未运行）
docker-compose up -d mysql

REM 等待MySQL启动完成
timeout /t 5 /nobreak > nul

REM 启动后端服务
echo 启动后端服务...
start cmd /k "cd backend && mvn spring-boot:run"

REM 启动前端服务
echo 启动前端服务...
cd frontend && npm run dev

pause