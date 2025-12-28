@echo off
COLOR 0C
echo.
echo ========================================
echo    FIX: Admin Role Issue
echo ========================================
echo.

echo This script will help diagnose and fix the "Current role: none" issue
echo.
echo Step 1: Testing current login response...
echo.

curl -s -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}" > response.json

echo Response saved to response.json
echo.
type response.json
echo.
echo.

echo ========================================
echo    ANALYSIS
echo ========================================
echo.
echo Look at the response above. Check for:
echo.
echo 1. Does it have "user": { ... } ?
echo 2. Does user object have "role": "ROLE_ADMIN" ?
echo 3. Is "role" null or missing?
echo.

set /p CONTINUE="Do you see a role in the response? (y/n): "

if /i "%CONTINUE%"=="n" (
    echo.
    echo ========================================
    echo    POSSIBLE ISSUES
    echo ========================================
    echo.
    echo 1. Backend not restarted after code changes
    echo 2. Database missing columns
    echo 3. Admin user doesn't have role set
    echo.
    echo SOLUTIONS:
    echo.
    echo Solution 1: RESTART BACKEND
    echo    - Stop current backend (Ctrl+C^)
    echo    - Run: RUN.bat
    echo.
    echo Solution 2: CHECK DATABASE
    echo    - Open MySQL Workbench
    echo    - Run: diagnose-admin.sql
    echo    - Check if admin has role = 'ROLE_ADMIN'
    echo.
    echo Solution 3: RECREATE ADMIN
    echo    - Run: mysql -u root
    echo    - Run: USE gym;
    echo    - Run: DELETE FROM users WHERE username = 'admin';
    echo    - Restart backend (RUN.bat^)
    echo.
) else (
    echo.
    echo Great! The role is in the response.
    echo.
    echo The issue might be in your frontend code.
    echo Check that your frontend is reading: data.user.role
    echo.
    echo Example:
    echo   const data = await response.json(^);
    echo   console.log('Role:', data.user.role^);
    echo.
)

echo.
echo ========================================
echo    NEXT STEPS
echo ========================================
echo.
echo 1. Check response.json file
echo 2. Verify backend is running latest code
echo 3. Run diagnose-admin.sql in MySQL
echo 4. If needed, restart backend
echo.
pause

del response.json 2>nul

