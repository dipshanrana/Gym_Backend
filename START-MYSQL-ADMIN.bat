@echo off
echo ====================================
echo MySQL Service Starter
echo ====================================
echo.
echo Attempting to start MySQL93 service...
echo.

net start MySQL93

if %errorlevel% equ 0 (
    echo.
    echo ✓ MySQL started successfully!
    echo.
    echo You can now run the Spring Boot application.
) else (
    echo.
    echo ✗ Failed to start MySQL!
    echo.
    echo This batch file needs Administrator privileges.
    echo.
    echo Please follow these steps:
    echo   1. Right-click this file (START-MYSQL.bat)
    echo   2. Select "Run as Administrator"
    echo.
    echo OR manually start MySQL from Services:
    echo   1. Press Win + R
    echo   2. Type: services.msc
    echo   3. Find "MySQL93" and click Start
    echo.
)

pause
d