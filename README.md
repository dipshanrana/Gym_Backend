# 🏋️ Gym Backend - Spring Boot REST API

Complete authentication backend for your Gym Management System with JWT authentication, user registration, and login functionality.

---

## 🚀 Quick Start (3 Steps!)

### 1️⃣ Start MySQL
**Right-click `START-MYSQL-ADMIN.bat` → Run as Administrator**

### 2️⃣ Start Backend
**Double-click `RUN.bat`**

### 3️⃣ Done! 
Backend is running on **http://localhost:8080** ✅

---

## 📋 What's Working

- ✅ User Registration with validation
- ✅ User Login with JWT tokens
- ✅ Password encryption (BCrypt)
- ✅ CORS enabled for frontend
- ✅ MySQL database integration
- ✅ Comprehensive error handling
- ✅ Field validation messages

---

## 🔌 API Endpoints

### Register User
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**Response (201):**
```json
{
  "message": "User registered successfully"
}
```

### Login User
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "johndoe",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

---

## 🔍 Validation Rules

| Field | Rules |
|-------|-------|
| **Username** | Required, min 3 chars, alphanumeric + underscore, unique |
| **Email** | Required, valid email format, unique |
| **Password** | Required, min 8 characters |
| **Confirm Password** | Required, must match password |

---

## 🗄️ Database

- **Type:** MySQL
- **Name:** `gym`
- **User:** `root`
- **Password:** (empty)
- **Port:** 3306

Tables are auto-created on startup.

---

## 🛠️ Manual Commands

### Start MySQL (Admin CMD)
```cmd
net start MySQL93
```

### Run Backend
```cmd
.\mvnw.cmd spring-boot:run
```

### Build Project
```cmd
.\mvnw.cmd clean install
```

### Run Tests
```cmd
.\mvnw.cmd test
```

---

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| MySQL won't start | Run `START-MYSQL-ADMIN.bat` as Administrator |
| Port 8080 in use | Change port in `application.properties` |
| CORS errors | Ensure backend is running and frontend on port 3000 |
| Login fails | Check MySQL is running and credentials are correct |

---

## 📁 Project Structure

```
gym-backend/
├── RUN.bat                          # Quick start script
├── START-MYSQL-ADMIN.bat            # MySQL starter
├── FIXES-COMPLETED.md               # Detailed documentation
├── src/main/java/com/gym/demo/
│   ├── controller/                  # REST endpoints
│   ├── service/                     # Business logic
│   ├── repository/                  # Database access
│   ├── model/                       # JPA entities
│   ├── dto/                         # Data transfer objects
│   ├── security/                    # JWT & Security config
│   ├── config/                      # App configuration
│   └── exception/                   # Error handling
└── src/main/resources/
    └── application.properties       # Configuration
```

---

## 🔐 Security Features

- ✅ Password encryption with BCrypt
- ✅ JWT token-based authentication
- ✅ CORS protection
- ✅ SQL injection prevention (JPA)
- ✅ XSS protection (Spring Security)
- ✅ Session management (Stateless)

---

## 📞 Need Help?

1. Check `FIXES-COMPLETED.md` for detailed documentation
2. Ensure MySQL service is running
3. Check application logs in terminal
4. Verify database connection

---

## 🎯 Next Steps

Once backend is running:
1. Start your Next.js frontend on port 3000
2. Test registration at http://localhost:3000/register
3. Test login at http://localhost:3000/login
4. Use the JWT token for protected routes

---

## 📝 Configuration

Edit `src/main/resources/application.properties` to change:
- Database credentials
- Server port
- JWT secret & expiration
- Log levels

---

**Made with ❤️ for Gym Management System**

