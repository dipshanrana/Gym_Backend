



















































@echo off
COLOR 0E
echo.
echo ========================================
echo    TESTING ROLE FIX
echo ========================================
echo.

echo [Test 1] Testing Admin Login with Role...
echo.
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"
echo.
echo.

echo ========================================
echo    WHAT TO LOOK FOR
echo ========================================
echo.
echo You should see a JSON response with:
echo   - "token": "eyJ..."
echo   - "tokenType": "Bearer"
echo   - "role": "ROLE_ADMIN"    ^^^<--- THIS IS THE FIX!
echo.
echo If you see the role field, the fix worked! ✅
echo If you DON'T see the role field, restart backend.
echo.
pause

