@echo off
echo ====================================
echo Starting Gym Application
echo ====================================
echo.

echo Step 1: Checking MySQL Service...
sc query MySQL93 | find "RUNNING" >nul
if %errorlevel% neq 0 (
    echo MySQL is not running. Please start MySQL manually:
    echo   1. Open Command Prompt as Administrator
    echo   2. Run: net start MySQL93
    echo.
    echo OR run this batch file as Administrator
    echo.
    pause
) else (
    echo MySQL is running!
    echo.
)

echo Step 2: Starting Spring Boot Application...
echo.
call mvnw.cmd spring-boot:run

pause

