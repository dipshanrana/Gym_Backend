# 🔍 ADMIN LOGIN TROUBLESHOOTING GUIDE

## Common Issue: Admin Not Logging In

If you can't login with admin credentials, follow these steps:

---

## ✅ Step 1: Verify Application Started Successfully

Check your console output for:
```
✅ ADMIN USER CREATED SUCCESSFULLY!
📧 Email:    admin@fitfuel.com
👤 Username: admin
🔑 Password: Ad****23
```

**If you DON'T see this:**
- Admin user might already exist
- Check for error messages in console
- Verify MySQL is running

**If you see "Admin user 'admin' already exists":**
- Admin was created in a previous run
- Proceed to Step 2

---

## ✅ Step 2: Verify Admin Exists in Database

### Option A: Using MySQL Workbench
1. Open MySQL Workbench
2. Connect to localhost
3. Run these queries:

```sql
USE gym;
SELECT * FROM users WHERE username = 'admin';
SELECT * FROM users WHERE email = 'admin@fitfuel.com';
```

**Expected Result:**
```
id | username | email              | password      | fullname              | enabled | role
1  | admin    | admin@fitfuel.com  | $2a$10$...   | System Administrator | 1       | ROLE_ADMIN
```

### Option B: Using Command Line
```bash
mysql -u root -p
USE gym;
SELECT id, username, email, role, enabled FROM users WHERE username = 'admin';
```

**If admin user is NOT in database:**
- Application didn't create it
- Check application logs for errors
- See "Common Errors" section below

**If admin user EXISTS:**
- Proceed to Step 3

---

## ✅ Step 3: Test Login with cURL

```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"
```

### Possible Responses:

**✅ Success:**
```json
{
  "token": "eyJhbGci...",
  "tokenType": "Bearer"
}
```
→ Admin login works! Issue is with frontend.

**❌ Invalid Credentials:**
```json
{
  "message": "Invalid username or password",
  "error": "Invalid username or password",
  "status": 401
}
```
→ Password mismatch. See Step 4.

**❌ Validation Error:**
```json
{
  "message": "Validation failed",
  "errors": {
    "usernameOrEmail": "Username or email is required"
  }
}
```
→ Request format issue.

**❌ Connection Refused:**
→ Backend not running. Start with `RUN.bat`

---

## ✅ Step 4: Reset Admin Password

### If password doesn't work, reset it:

#### Option A: Delete and Recreate
```sql
USE gym;
DELETE FROM users WHERE username = 'admin';
```
Then restart your application - admin will be recreated.

#### Option B: Update Password Directly
```sql
USE gym;
-- This sets password to "Admin@123"
UPDATE users 
SET password = '$2a$10$rXj5aBfZQBK7fCQn3aVBZ.KN5pKQI5eFOJ9q7kkKKxNWx3V3xjC3S'
WHERE username = 'admin';
```

#### Option C: Use BCrypt Online Tool
1. Go to https://bcrypt-generator.com/
2. Enter your desired password
3. Rounds: 10
4. Generate hash
5. Update database:
```sql
UPDATE users SET password = 'PASTE_HASH_HERE' WHERE username = 'admin';
```

---

## ✅ Step 5: Check Frontend Login Code

Your frontend should send:
```javascript
{
  "usernameOrEmail": "admin",  // or "admin@fitfuel.com"
  "password": "Admin@123"
}
```

**Common Frontend Issues:**

### Issue: Sending wrong field names
```javascript
// ❌ WRONG
{ "username": "admin", "password": "Admin@123" }

// ✅ CORRECT
{ "usernameOrEmail": "admin", "password": "Admin@123" }
```

### Issue: Extra spaces in password
```javascript
// ❌ WRONG - has trailing space
{ "usernameOrEmail": "admin", "password": "Admin@123 " }

// ✅ CORRECT
{ "usernameOrEmail": "admin", "password": "Admin@123" }
```

### Issue: Case sensitivity
```javascript
// ❌ WRONG - wrong case
{ "usernameOrEmail": "Admin", "password": "admin@123" }

// ✅ CORRECT - exact match
{ "usernameOrEmail": "admin", "password": "Admin@123" }
```

---

## 🐛 Common Errors and Solutions

### Error 1: "Admin user already exists"
**Cause:** Admin was created in a previous run  
**Solution:** This is normal! Admin exists. Try logging in.

### Error 2: "Communications link failure"
**Cause:** MySQL not running  
**Solution:**
```bash
# Run as Administrator
net start MySQL93
```

### Error 3: "Access denied for user 'root'"
**Cause:** MySQL password incorrect in application.properties  
**Solution:** Update password in `application.properties`:
```properties
spring.datasource.password=your_mysql_password
```

### Error 4: "Table 'gym.users' doesn't exist"
**Cause:** Database not created or schema not updated  
**Solution:**
1. Ensure `spring.jpa.hibernate.ddl-auto=update` in application.properties
2. Restart application
3. Or manually create table:
```sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  fullname VARCHAR(255),
  enabled BOOLEAN DEFAULT TRUE,
  role VARCHAR(50) DEFAULT 'ROLE_USER'
);
```

### Error 5: "Invalid username or password" but credentials are correct
**Cause:** Multiple possible reasons  
**Solutions:**
1. Check password has no extra spaces
2. Verify username is exactly "admin" (lowercase)
3. Ensure password is exactly "Admin@123" (case-sensitive)
4. Reset admin password (see Step 4)
5. Check database for actual stored values

### Error 6: CORS error in browser
**Cause:** CORS not configured or backend not running  
**Solution:**
1. Ensure backend is running on port 8080
2. Ensure frontend is on port 3000
3. Restart backend

### Error 7: "enabled" or "fullname" column doesn't exist
**Cause:** Database schema not updated  
**Solution:**
```sql
ALTER TABLE users ADD COLUMN fullname VARCHAR(255);
ALTER TABLE users ADD COLUMN enabled BOOLEAN DEFAULT TRUE;
```
Or restart app (JPA will auto-update if ddl-auto=update)

---

## 🔍 Debug Checklist

Run through this checklist:

- [ ] MySQL service is running
- [ ] Backend application started successfully
- [ ] Saw admin creation message in console (or "already exists")
- [ ] Database `gym` exists
- [ ] Table `users` exists
- [ ] Admin user row exists in `users` table
- [ ] Admin user has role = 'ROLE_ADMIN'
- [ ] Admin user has enabled = true
- [ ] cURL login test works
- [ ] Frontend sends correct field names
- [ ] Frontend sends correct password (no extra spaces)
- [ ] CORS is not blocking (check browser console)
- [ ] Using correct URL (http://localhost:8080/api/auth/login)

---

## 🧪 Quick Diagnosis Commands

### 1. Check MySQL is Running
```bash
sc query MySQL93
```
Should show: **RUNNING**

### 2. Check Backend is Running
```bash
curl http://localhost:8080/api/auth/login
```
Should return: **400 validation error** (this is good - means backend is running)

### 3. Check Database
```bash
mysql -u root -p
USE gym;
SHOW TABLES;
SELECT COUNT(*) FROM users;
```

### 4. Check Admin User
```bash
mysql -u root -p
USE gym;
SELECT id, username, email, role, enabled FROM users WHERE username = 'admin';
```

### 5. Test Login
```bash
curl -v -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"
```

---

## 🔧 Manual Admin Creation (Last Resort)

If automatic creation fails, create admin manually:

```sql
USE gym;

-- Delete existing admin if any
DELETE FROM users WHERE username = 'admin';

-- Create admin user
INSERT INTO users (username, email, password, fullname, enabled, role) 
VALUES (
  'admin', 
  'admin@fitfuel.com',
  '$2a$10$rXj5aBfZQBK7fCQn3aVBZ.KN5pKQI5eFOJ9q7kkKKxNWx3V3xjC3S',  -- Admin@123
  'System Administrator',
  true,
  'ROLE_ADMIN'
);

-- Verify
SELECT * FROM users WHERE username = 'admin';
```

---

## 📞 Still Not Working?

### Check Application Logs
Look for these in your console:
- Exceptions or stack traces
- "Failed to create admin user"
- Database connection errors
- JPA/Hibernate errors

### Enable Debug Logging
Add to `application.properties`:
```properties
logging.level.com.gym.demo=DEBUG
logging.level.org.springframework.security=DEBUG
```

Restart and check logs for detailed information.

### Verify Configuration
Check `application.properties`:
```properties
# Should have these
admin.username=${ADMIN_USERNAME:admin}
admin.email=${ADMIN_EMAIL:admin@fitfuel.com}
admin.password=${ADMIN_PASSWORD:Admin@123}
admin.fullname=${ADMIN_FULLNAME:System Administrator}
admin.enabled=true
```

---

## ✅ Working Configuration

If everything is correct, you should be able to:

1. **Start application** → See admin creation message
2. **Login with cURL** → Get JWT token
3. **Login from frontend** → Redirect to dashboard
4. **Access admin endpoints** → Get user list

**Test Command:**
```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"Admin@123"}'

# Save the token from response

# 2. Access admin endpoint
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 📝 Summary

**Default Admin Credentials:**
- Email: `admin@fitfuel.com`
- Username: `admin`
- Password: `Admin@123`
- Role: `ROLE_ADMIN`

**Most Common Issues:**
1. MySQL not running
2. Password has extra spaces
3. Wrong field name in frontend (use `usernameOrEmail`)
4. Admin user not created (check logs)
5. Database schema not updated (missing columns)

**Quick Fix:**
```sql
-- Delete admin and restart app
DELETE FROM users WHERE username = 'admin';
-- Then restart your Spring Boot app
```

**Your admin system should work perfectly now! 🚀**

