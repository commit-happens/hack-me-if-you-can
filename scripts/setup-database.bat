@echo off
REM Database Setup Script for Windows
REM This is the Windows version of setup-database.sh
REM
REM USAGE:
REM Double-click this file or run from Command Prompt/PowerShell

echo 🗄️  Hack Me If You Can - Database Setup (Windows)
echo ==============================================

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker is not installed. Please install Docker Desktop first.
    echo Visit: https://docs.docker.com/desktop/windows/
    pause
    exit /b 1
)

docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Docker Compose is not installed. Please install Docker Compose first.
    pause
    exit /b 1
)

echo ✅ Docker is installed

REM Navigate to project root
cd /d "%~dp0.."

echo 📁 Project root: %CD%

REM Check if docker-compose.yml exists
if not exist "docker-compose.yml" (
    echo ❌ docker-compose.yml not found in project root
    pause
    exit /b 1
)

REM Stop any existing containers
echo 🛑 Stopping existing containers...
docker-compose down -v

REM Start the database services
echo 🚀 Starting database services...
docker-compose up -d mysql

REM Wait for MySQL to be ready
echo ⏳ Waiting for MySQL to be ready...
timeout /t 10 /nobreak

REM Start web interfaces
echo 🌐 Starting web interfaces...
docker-compose up -d phpmyadmin adminer

REM Wait for web interfaces
timeout /t 5 /nobreak

echo.
echo 🎉 Database setup completed successfully!
echo ==============================================
echo.
echo 📊 Database Connection Info:
echo    Host: localhost
echo    Port: 3306
echo    Database: hackme_db
echo    Username: hackme_user
echo    Password: hackme_password123
echo    Root Password: rootpassword123
echo.
echo 🌐 Web Interfaces:
echo    phpMyAdmin: http://localhost:8081
echo    Adminer:    http://localhost:8082
echo.
echo 🔧 Useful Commands:
echo    Stop services:    docker-compose down
echo    View logs:        docker-compose logs -f mysql
echo    Reset database:   docker-compose down -v
echo    Restart services: docker-compose restart
echo.
echo ☕ Next Steps:
echo 1. Update backend\src\main\resources\application.properties
echo 2. Change spring.datasource.url to: jdbc:mysql://localhost:3306/hackme_db
echo 3. Set spring.datasource.username to: hackme_user
echo 4. Set spring.datasource.password to: hackme_password123
echo 5. Start your Spring Boot application: mvn spring-boot:run
echo.
echo Happy coding! 🚀
pause