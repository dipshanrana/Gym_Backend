# 🔐 ADMIN USER - QUICK REFERENCE

## Default Credentials
```
Email:    admin@fitfuel.com
Username: admin
Password: Admin@123
Role:     ROLE_ADMIN
```

## Quick Test
```bash
# 1. Login as admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"Admin@123"}'

# Response: {"token":"eyJ...","tokenType":"Bearer"}

# 2. Get all users (use token from above)
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN"

# 3. Get stats
curl -X GET http://localhost:8080/api/admin/stats \
  -H "Authorization: Bearer YOUR_TOKEN"

# 4. Change password
curl -X POST http://localhost:8080/api/admin/change-password \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","currentPassword":"Admin@123","newPassword":"NewPass123!"}'
```

## Production Override
```bash
# Set environment variables before starting
export ADMIN_USERNAME=myadmin
export ADMIN_EMAIL=admin@mycompany.com
export ADMIN_PASSWORD=SecurePass123!
export ADMIN_FULLNAME=My Administrator

# Then start app
java -jar gym-backend.jar
```

## Security Checklist
- [ ] Change default password after first login
- [ ] Use environment variables in production
- [ ] Use strong password (12+ chars, mixed)
- [ ] Don't commit credentials to git
- [ ] Limit admin accounts
- [ ] Monitor admin access

## Files Modified
✅ User.java - Added fullname, enabled fields
✅ application.properties - Added admin config
✅ DataInitializer.java - Auto-creates admin
✅ AdminController.java - Admin endpoints

## What Happens on Startup
1. App checks if admin exists
2. If not, creates admin user
3. Password encrypted with BCrypt
4. Credentials logged to console (password masked)
5. Ready to login!

See ADMIN-SETUP-GUIDE.md for complete documentation.

