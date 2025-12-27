m@echo off
COLOR 0A
echo.
echo ========================================
echo    GYM BACKEND - QUICK START
echo ========================================
echo.
echo Step 1: Checking MySQL Service...
echo.

sc query MySQL93 | find "RUNNING" >nul
if %errorlevel% neq 0 (
    echo [ERROR] MySQL is NOT running!
    echo.
    echo Please start MySQL first:
    echo   1. Right-click "START-MYSQL-ADMIN.bat"
    echo   2. Select "Run as Administrator"
    echo.
    echo OR open Command Prompt as Admin and run:
    echo   net start MySQL93
    echo.
    pause
    exit /b 1
)

echo [SUCCESS] MySQL is running!
echo.
echo ========================================
echo Step 2: Starting Spring Boot Application
echo ========================================
echo.
echo Please wait... This may take 30-60 seconds
echo.

call mvnw.cmd spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Failed to start application!
    echo Check the error messages above.
    echo.
    pause
    exit /b 1
)

pause

