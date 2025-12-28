@echo off
pause
echo.
echo 2. Run TEST-CHANGE-PASSWORD.bat with that token
echo 1. Copy the token from the response above
echo Next steps:
echo.
echo If you see this, the feature is working!
echo.
echo Look for "passwordChangeRequired": true in the response
echo.
echo ========================================
echo    WHAT TO CHECK
echo ========================================

echo.
echo.
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"
  -H "Content-Type: application/json" ^
curl -X POST http://localhost:8080/api/auth/login ^
echo.
echo Step 1: Testing Admin Login (First Time)

echo.
echo ========================================
echo    TEST FORCE PASSWORD CHANGE
echo ========================================
echo.
COLOR 0E

