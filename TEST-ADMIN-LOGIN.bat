@echo off
COLOR 0B
echo.
echo ========================================
echo    ADMIN LOGIN DIAGNOSTIC TOOL
echo ========================================
echo.

echo [Step 1] Checking MySQL Service...
sc query MySQL93 | find "RUNNING" >nul
if %errorlevel% equ 0 (
    echo ✅ MySQL is RUNNING
) else (
    echo ❌ MySQL is NOT RUNNING
    echo    Please start MySQL first: START-MYSQL-ADMIN.bat
    echo.
    pause
    exit /b 1
)
echo.

echo [Step 2] Testing Backend Connection...
curl -s http://localhost:8080/api/auth/login >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Backend is RUNNING on port 8080
) else (
    echo ❌ Backend is NOT RUNNING
    echo    Please start backend: RUN.bat
    echo.
    pause
    exit /b 1
)
echo.

echo [Step 3] Testing Admin Login...
echo.
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"
echo.
echo.

echo ========================================
echo    DIAGNOSTIC COMPLETE
echo ========================================
echo.
echo If you see a JWT token above, admin login works! ✅
echo If you see an error, check ADMIN-LOGIN-TROUBLESHOOTING.md
echo.
pause

