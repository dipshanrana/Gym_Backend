@echo off
COLOR 0B
echo.
echo ========================================
echo    TEST PASSWORD CHANGE
echo ========================================
echo.

set /p TOKEN="Enter the JWT token from previous step: "
echo.

echo Testing Password Change...
echo.
curl -X POST http://localhost:8080/api/user/change-password ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %TOKEN%" ^
  -d "{\"currentPassword\":\"Admin@123\",\"newPassword\":\"NewSecure123!\",\"confirmPassword\":\"NewSecure123!\"}"
echo.
echo.

echo ========================================
echo    RESULT
echo ========================================
echo.
echo If successful, you should see:
echo {"message":"Password changed successfully","success":true}
echo.
echo Now try logging in again with the new password!
echo.
pause

