# 🔐 Admin User Setup - Complete Guide

## ✅ Admin User Configuration Completed!

Your backend now automatically creates a default admin user on startup with secure configuration.

---

## 🎯 Default Admin Credentials

### 📧 Email: `admin@fitfuel.com`
### 👤 Username: `admin`
### 🔑 Password: `Admin@123`
### 👔 Role: `ROLE_ADMIN`

**⚠️ IMPORTANT: Change these credentials after first login!**

---

## 🚀 How It Works

### 1. Automatic Creation
When you start the application, it automatically:
- ✅ Checks if admin user exists
- ✅ Creates admin if not found
- ✅ Encrypts password with BCrypt
- ✅ Assigns ROLE_ADMIN
- ✅ Logs credentials to console

### 2. What You'll See on Startup
```
========================================
✅ ADMIN USER CREATED SUCCESSFULLY!
========================================
📧 Email:    admin@fitfuel.com
👤 Username: admin
🔑 Password: Ad****23
👔 Role:     ROLE_ADMIN
========================================
⚠️  IMPORTANT: Change the admin password after first login!
⚠️  For production, use environment variables for credentials!
========================================
```

---

## 🔧 Configuration Files

### 1. application.properties
```properties
# Admin User Configuration (Use environment variables in production)
admin.username=${ADMIN_USERNAME:admin}
admin.email=${ADMIN_EMAIL:admin@fitfuel.com}
admin.password=${ADMIN_PASSWORD:Admin@123}
admin.fullname=${ADMIN_FULLNAME:System Administrator}
admin.enabled=true
```

**What this means:**
- `${ADMIN_USERNAME:admin}` → Uses env variable `ADMIN_USERNAME` if set, otherwise defaults to `admin`
- Same pattern for all other fields
- Allows easy override without code changes

---

## 🔒 Security Features

### 1. Environment Variable Support ✅
**Production Usage:**
```bash
# Windows
set ADMIN_USERNAME=superadmin
set ADMIN_EMAIL=admin@yourcompany.com
set ADMIN_PASSWORD=YourSecurePassword123!
set ADMIN_FULLNAME=Super Administrator

# Linux/Mac
export ADMIN_USERNAME=superadmin
export ADMIN_EMAIL=admin@yourcompany.com
export ADMIN_PASSWORD=YourSecurePassword123!
export ADMIN_FULLNAME=Super Administrator
```

Then start your app - it will use these values instead of defaults!

### 2. Password Encryption ✅
- Uses BCrypt hashing algorithm
- Automatic salt generation
- Password strength: 10 rounds
- Original password never stored

### 3. Password Masking in Logs ✅
- Passwords are masked in console output
- Shows only first 2 and last 2 characters
- Example: `Admin@123` → `Ad****23`

### 4. Duplicate Prevention ✅
- Checks if username exists before creating
- Checks if email exists before creating
- Won't create duplicate admins

---

## 📡 Admin API Endpoints

### 1. GET /api/admin/users
**Get all users (Admin only)**

**Request:**
```http
GET http://localhost:8080/api/admin/users
Authorization: Bearer <admin-jwt-token>
```

**Response:**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@fitfuel.com",
    "fullname": "System Administrator",
    "role": "ROLE_ADMIN",
    "enabled": true
  },
  {
    "id": 2,
    "username": "john",
    "email": "john@test.com",
    "fullname": null,
    "role": "ROLE_USER",
    "enabled": true
  }
]
```

### 2. GET /api/admin/stats
**Get user statistics (Admin only)**

**Request:**
```http
GET http://localhost:8080/api/admin/stats
Authorization: Bearer <admin-jwt-token>
```

**Response:**
```json
{
  "totalUsers": 5,
  "adminUsers": 1,
  "regularUsers": 4
}
```

### 3. POST /api/admin/change-password
**Change admin password (Admin only)**

**Request:**
```http
POST http://localhost:8080/api/admin/change-password
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json

{
  "username": "admin",
  "currentPassword": "Admin@123",
  "newPassword": "NewSecurePassword123!"
}
```

**Success Response:**
```json
{
  "message": "Password changed successfully"
}
```

**Error Response:**
```json
{
  "error": "Current password is incorrect"
}
```

---

## 🧪 Testing the Admin User

### Step 1: Start Your Application
```bash
# Start MySQL first
START-MYSQL-ADMIN.bat

# Start backend
RUN.bat
```

Look for the admin creation message in console ✅

### Step 2: Login as Admin
```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

### Step 3: Use Admin Token
```bash
# Get all users
curl -X GET http://localhost:8080/api/admin/users ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# Get stats
curl -X GET http://localhost:8080/api/admin/stats ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Step 4: Change Admin Password
```bash
curl -X POST http://localhost:8080/api/admin/change-password ^
  -H "Authorization: Bearer YOUR_TOKEN_HERE" ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"currentPassword\":\"Admin@123\",\"newPassword\":\"NewPassword123!\"}"
```

---

## 🔑 User Roles

### ROLE_ADMIN
- ✅ Full access to all endpoints
- ✅ Can view all users
- ✅ Can view statistics
- ✅ Can change passwords
- ✅ Can access admin panel (when you build it)

### ROLE_USER
- ✅ Can login
- ✅ Can access user-specific features
- ❌ Cannot access admin endpoints
- ❌ Cannot view other users

---

## 📊 Database Schema Updates

The `users` table now includes:

```sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  fullname VARCHAR(255),           -- NEW
  enabled BOOLEAN DEFAULT TRUE,    -- NEW
  role VARCHAR(50) DEFAULT 'ROLE_USER',
  
  INDEX idx_username (username),
  INDEX idx_email (email),
  INDEX idx_role (role)            -- NEW
);
```

**New Fields:**
- `fullname` - User's full name (optional)
- `enabled` - Account status (true = active, false = disabled)

---

## 🛡️ Production Deployment

### Option 1: Environment Variables (Recommended)
```bash
# Set in your hosting platform (Heroku, AWS, etc.)
ADMIN_USERNAME=your-admin
ADMIN_EMAIL=admin@yourcompany.com
ADMIN_PASSWORD=VerySecurePassword123!
ADMIN_FULLNAME=Company Administrator
```

### Option 2: Application Properties Override
Create `application-prod.properties`:
```properties
admin.username=production-admin
admin.email=admin@production.com
admin.password=${ADMIN_PASSWORD}
admin.fullname=Production Admin
```

Run with:
```bash
java -jar gym-backend.jar --spring.profiles.active=prod
```

### Option 3: Docker Environment
```dockerfile
FROM openjdk:21-slim
ENV ADMIN_USERNAME=dockeradmin
ENV ADMIN_EMAIL=admin@docker.com
ENV ADMIN_PASSWORD=${ADMIN_PASSWORD}
COPY target/gym-backend.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## ⚠️ Security Best Practices

### DO ✅
- Use environment variables in production
- Change default password immediately
- Use strong passwords (min 12 chars, mixed case, numbers, symbols)
- Regularly rotate admin passwords
- Monitor admin access logs
- Limit admin accounts to necessary personnel

### DON'T ❌
- Use default credentials in production
- Commit passwords to version control
- Share admin credentials
- Use weak passwords
- Leave admin credentials in application.properties for production
- Create multiple admin accounts unnecessarily

---

## 🔄 Password Change Process

### Via API (Recommended)
```javascript
// Frontend example
const changeAdminPassword = async () => {
  const response = await fetch('http://localhost:8080/api/admin/change-password', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${adminToken}`
    },
    body: JSON.stringify({
      username: 'admin',
      currentPassword: 'Admin@123',
      newPassword: 'NewSecurePassword123!'
    })
  });
  
  const data = await response.json();
  console.log(data.message);
};
```

### Via Database (Manual - Last Resort)
```sql
-- Generate new BCrypt hash using online tool or Java code
-- Then update database directly
UPDATE users 
SET password = '$2a$10$newHashedPasswordHere' 
WHERE username = 'admin';
```

---

## 📁 Files Created/Modified

### Created Files:
1. **DataInitializer.java** - Auto-creates admin user on startup
2. **AdminController.java** - Admin-specific REST endpoints

### Modified Files:
1. **User.java** - Added `fullname` and `enabled` fields
2. **application.properties** - Added admin configuration

### Unchanged (Already Secure):
1. **SecurityConfig.java** - Already has `@EnableMethodSecurity`
2. **JwtUtil.java** - Token generation works for admin too
3. **CustomUserDetailsService.java** - Handles role-based auth

---

## 🎯 Summary

**What You Have Now:**
- ✅ Auto-created admin user on startup
- ✅ Secure password encryption (BCrypt)
- ✅ Environment variable support
- ✅ Admin-only API endpoints
- ✅ Password change functionality
- ✅ User statistics endpoint
- ✅ Role-based access control

**Admin Credentials:**
- Email: `admin@fitfuel.com`
- Username: `admin`
- Password: `Admin@123`
- Role: `ROLE_ADMIN`

**Next Steps:**
1. Start your application
2. Look for admin creation message in console
3. Login with admin credentials
4. Change password immediately
5. Test admin endpoints

---

## 🚀 Quick Start

```bash
# 1. Start MySQL
START-MYSQL-ADMIN.bat

# 2. Start Backend
RUN.bat

# 3. Login as admin
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"usernameOrEmail\":\"admin\",\"password\":\"Admin@123\"}"

# 4. Use the token to access admin endpoints
```

**Your admin system is ready! 🎊**

