@echo off
echo Starting Menstrual Mate development environment...

REM Check if Docker is running
docker --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not installed or not running. Please start Docker Desktop.
    pause
    exit /b 1
)

REM Start MySQL container
echo Starting MySQL container...
docker-compose up -d mysql

REM Wait for MySQL to fully start
echo Waiting for MySQL to start...
timeout /t 10 /nobreak > nul

REM Check if MySQL started successfully
docker-compose ps | findstr "menstrual-mate-mysql" | findstr "Up" >nul
if errorlevel 1 (
    echo Error: MySQL container failed to start
    pause
    exit /b 1
)

REM Check backend dependencies
echo Checking backend dependencies...
cd /d "%~dp0backend"
if not exist "pom.xml" (
    echo Error: backend/pom.xml not found
    pause
    exit /b 1
)

REM Start backend service
echo Starting backend service...
start "Backend" cmd /k "cd /d "%~dp0backend" && mvn spring-boot:run"

REM Check frontend dependencies
cd /d "%~dp0frontend"
if not exist "package.json" (
    echo Error: frontend/package.json not found
    pause
    exit /b 1
)

REM Install frontend dependencies if needed
if not exist "node_modules" (
    echo Installing frontend dependencies...
    call npm install
)

REM Start frontend service
echo Starting frontend service...
start "Frontend" cmd /k "cd /d "%~dp0frontend" && npm run dev"

echo.
echo Environment started successfully!
echo Backend API: http://localhost:8080
echo Frontend: http://localhost:5173
echo Swagger: http://localhost:8080/swagger-ui.html
echo.
echo Press any key to close this window...
pause > nul