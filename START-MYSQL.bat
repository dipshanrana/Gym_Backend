@echo off
echo Starting MySQL93 service...
net start MySQL93
if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo MySQL93 service started successfully!
    echo ========================================
    echo.
    echo You can now run your Spring Boot application:
    echo cd C:\Users\dipsh\Downloads\Gym_backend
    echo .\mvnw.cmd spring-boot:run
    echo.
) else (
    echo.
    echo ========================================
    echo ERROR: Could not start MySQL93 service
    echo ========================================
    echo.
    echo This script must be run as Administrator!
    echo Right-click this file and select "Run as administrator"
    echo.
)
pause

