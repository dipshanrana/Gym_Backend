# Gym Backend - Quick Start Guide

## 🔧 Fixed Issues

1. ✅ **MySQL Dialect Error** - Fixed Hibernate dialect configuration
2. ✅ **AuthResponse Constructor** - Fixed to accept token and tokenType
3. ✅ **Demo Entity @Id** - Added @Id annotation
4. ✅ **CORS Configuration** - Properly configured for frontend
5. ✅ **Validation Messages** - Updated to match frontend expectations
6. ✅ **Error Response Format** - Standardized JSON error responses

## 🚀 How to Start the Application

### Step 1: Start MySQL Service

**Option A: Using Command Prompt (Recommended)**
```bash
# Open Command Prompt as Administrator
# Right-click Command Prompt -> Run as Administrator
net start MySQL93
```

**Option B: Using Services**
1. Press `Win + R`
2. Type `services.msc` and press Enter
3. Find "MySQL93" in the list
4. Right-click and select "Start"

**Option C: Using PowerShell (Admin)**
```powershell
Start-Service -Name MySQL93
```

### Step 2: Verify MySQL Connection

Check if MySQL is running:
```bash
mysql -u root -p
# Press Enter (password is empty)
```

If you can't connect, check your MySQL password in `application.properties`:
```properties
spring.datasource.password=
```

### Step 3: Start the Spring Boot Application

**Option A: Using the provided batch file**
```bash
# Right-click START-APP.bat -> Run as Administrator
START-APP.bat
```

**Option B: Using Maven directly**
```bash
mvnw.cmd spring-boot:run
```

**Option C: Using your IDE**
- Run `GymApplication.java` main method

### Step 4: Test the API

The application will start on `http://localhost:8080`

**Test Registration:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"password123\",\"confirmPassword\":\"password123\"}"
```

**Test Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"usernameOrEmail\":\"testuser\",\"password\":\"password123\"}"
```

## 🔍 Common Errors and Solutions

### Error: "Connection refused: getsockopt"
**Cause:** MySQL is not running
**Solution:** Start MySQL service (see Step 1)

### Error: "Access denied for user 'root'@'localhost'"
**Cause:** MySQL password is incorrect
**Solution:** 
1. Update password in `src/main/resources/application.properties`
2. Or reset MySQL root password

### Error: "CORS policy: No 'Access-Control-Allow-Origin' header"
**Cause:** Backend not running or CORS not configured
**Solution:** 
- Ensure backend is running on port 8080
- CORS is already configured in `CorsConfig.java`

### Error: "Unable to determine Dialect"
**Cause:** MySQL not running or connection failed
**Solution:** Fixed in `application.properties` - ensure MySQL is running

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/gym/demo/
│   │   ├── GymApplication.java          # Main application
│   │   ├── config/
│   │   │   └── CorsConfig.java          # CORS configuration
│   │   ├── controller/
│   │   │   └── AuthController.java      # REST endpoints
│   │   ├── dto/
│   │   │   ├── AuthResponse.java        # Login response
│   │   │   ├── LoginDto.java            # Login request
│   │   │   └── RegisterDto.java         # Register request
│   │   ├── exception/
│   │   │   ├── BadRequestException.java # Custom exception
│   │   │   └── GlobalExceptionHandler.java # Error handling
│   │   ├── model/
│   │   │   ├── User.java                # User entity
│   │   │   └── Demo.java                # Demo entity
│   │   ├── repository/
│   │   │   └── UserRepository.java      # Database access
│   │   ├── security/
│   │   │   ├── CustomUserDetailsService.java
│   │   │   ├── JwtFilter.java           # JWT authentication
│   │   │   ├── JwtUtil.java             # JWT utilities
│   │   │   └── SecurityConfig.java      # Security configuration
│   │   └── service/
│   │       └── AuthService.java         # Business logic
│   └── resources/
│       └── application.properties       # Configuration
```

## 🔐 API Endpoints

### Public Endpoints (No Authentication Required)

#### POST /api/auth/register
Register a new user

**Request:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "confirmPassword": "password123"
}
```

**Success Response (201 Created):**
```json
{
  "message": "User registered successfully"
}
```

**Error Response (400 Bad Request):**
```json
{
  "message": "Validation failed",
  "errors": {
    "username": "Username is required",
    "email": "Please enter a valid email address"
  },
  "status": 400
}
```

OR

```json
{
  "message": "Username already taken",
  "error": "Username already taken",
  "status": 400
}
```

#### POST /api/auth/login
Login user

**Request:**
```json
{
  "usernameOrEmail": "johndoe",
  "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

**Error Response (400 Bad Request):**
```json
{
  "message": "Invalid credentials",
  "error": "Invalid credentials",
  "status": 400
}
```

## 🎯 Validation Rules

### Username
- ✅ Required
- ✅ Minimum 3 characters
- ✅ Can only contain letters, numbers, and underscores
- ✅ Must be unique

### Email
- ✅ Required
- ✅ Valid email format
- ✅ Must be unique

### Password
- ✅ Required
- ✅ Minimum 8 characters
- ✅ Must match confirmPassword

## 🌐 Frontend Integration

Your Next.js frontend on `http://localhost:3000` is already configured to work with this backend.

**Environment Variables** (if needed):
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

## 📝 Database Schema

The application will automatically create the following tables:

### users
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) DEFAULT 'ROLE_USER'
);
```

### demo
```sql
CREATE TABLE demo (
  id BIGINT PRIMARY KEY AUTO_INCREMENT
);
```

## 🛠️ Troubleshooting

### Check if backend is running:
```bash
curl http://localhost:8080/api/auth/login
```

### Check MySQL connection:
```bash
mysql -u root -p
SHOW DATABASES;
USE gym;
SHOW TABLES;
```

### View application logs:
The logs will show in your terminal where you ran the application.

### Clean and rebuild:
```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

## 📞 Support

If you encounter any issues:
1. Check that MySQL is running
2. Verify port 8080 is not in use
3. Check application logs for errors
4. Ensure database password is correct in application.properties

