# Gym Backend - User Authentication System

## Summary of Implementation

I've successfully implemented a complete user registration and login system with JWT authentication for your Spring Boot Gym application.

## What Was Fixed

### Root Cause of the Error
The error `ClassNotFoundException: org.hibernate.dialect.MySQL8Dialect` occurred because:
- You're using **Spring Boot 4.0.1** which includes **Hibernate 7.2.0**
- Starting from Hibernate 6.x, dialect class names were reorganized
- `MySQL8Dialect` was renamed to simply `MySQLDialect`

### Solution Applied
Updated `application.properties` to use the correct dialect class:
- **OLD**: `org.hibernate.dialect.MySQL8Dialect` ❌
- **NEW**: `org.hibernate.dialect.MySQLDialect` ✅

## Project Structure

```
demo/
├── src/main/java/com/gym/demo/
│   ├── GymApplication.java          # Main application
│   ├── controller/
│   │   ├── AuthController.java      # REST endpoints for /api/auth/register & /api/auth/login
│   │   └── TestController.java      # Protected test endpoint
│   ├── service/
│   │   └── AuthService.java         # Business logic for registration & login
│   ├── repository/
│   │   └── UserRepository.java      # JPA repository for User entity
│   ├── model/
│   │   ├── User.java                # User entity with JPA annotations
│   │   └── Demo.java                # (existing)
│   ├── dto/
│   │   ├── RegisterDto.java         # Registration request with validation
│   │   ├── LoginDto.java            # Login request with validation
│   │   └── AuthResponse.java        # Login response with JWT token
│   ├── security/
│   │   ├── SecurityConfig.java      # Spring Security configuration
│   │   ├── JwtUtil.java             # JWT token generation & validation
│   │   ├── JwtFilter.java           # JWT authentication filter
│   │   └── CustomUserDetailsService.java  # UserDetails loader
│   └── exception/
│       ├── BadRequestException.java        # Custom exception
│       └── GlobalExceptionHandler.java     # Global error handler
└── src/main/resources/
    ├── application.properties              # MySQL configuration (default)
    └── application-h2.properties           # H2 in-memory configuration (optional)
```

## Features Implemented

### ✅ User Registration
- Validates username, email, password
- Checks password confirmation match
- Prevents duplicate usernames/emails
- Hashes passwords with BCrypt
- Returns appropriate error messages

### ✅ User Login
- Accepts username or email
- Validates credentials
- Returns JWT token on success
- Token valid for 1 hour (configurable)

### ✅ Security
- JWT-based stateless authentication
- Protected endpoints require valid token
- Password encryption with BCrypt
- CSRF disabled for REST API
- Custom authentication filter

### ✅ Validation & Error Handling
- Field-level validation with annotations
- Custom validation messages
- Global exception handler
- Returns structured error responses

## How to Run

### Prerequisites
1. **MySQL Database**: Ensure MySQL is running on `localhost:3306`
2. **Database Created**: 
   ```sql
   CREATE DATABASE gym;
   ```
3. **Credentials**: Update in `application.properties` if needed

### Start the Application

**Using Maven Wrapper (recommended):**
```powershell
# From project root: C:\Users\dipsh\Downloads\Gym_backend\demo

# Build and run
.\mvnw.cmd spring-boot:run

# Or build JAR first
.\mvnw.cmd -DskipTests package
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

**Alternative - Using H2 (no MySQL needed):**
```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=h2
```

### Expected Startup Logs
```
Started GymApplication in X.XXX seconds
Tomcat started on port 8080
```

## API Endpoints

### 1. Register User
**POST** `/api/auth/register`

**Request:**
```json
{
  "username": "alice",
  "email": "alice@example.com",
  "password": "Password123",
  "confirmPassword": "Password123"
}
```

**Success Response (201):**
```json
"user registered"
```

**Error Response (400):**
```json
{
  "username": "username already taken"
}
```

### 2. Login
**POST** `/api/auth/login`

**Request:**
```json
{
  "usernameOrEmail": "alice",
  "password": "Password123"
}
```

**Success Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

**Error Response (400):**
```json
{
  "error": "invalid credentials"
}
```

### 3. Protected Endpoint (Example)
**GET** `/api/test/hello`

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Success Response (200):**
```
Hello, authenticated user
```

**Error Response (401):** If token missing or invalid

## Testing with cURL

### Register a New User
```powershell
curl -X POST http://localhost:8080/api/auth/register `
  -H "Content-Type: application/json" `
  -d '{\"username\":\"alice\",\"email\":\"alice@example.com\",\"password\":\"Password123\",\"confirmPassword\":\"Password123\"}'
```

### Login
```powershell
curl -X POST http://localhost:8080/api/auth/login `
  -H "Content-Type: application/json" `
  -d '{\"usernameOrEmail\":\"alice\",\"password\":\"Password123\"}'
```

### Access Protected Endpoint
```powershell
# Replace <TOKEN> with actual token from login response
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8080/api/test/hello
```

## Validation Rules

### RegisterDto
- **username**: Required, not blank
- **email**: Required, valid email format
- **password**: Required, minimum 8 characters
- **confirmPassword**: Required, must match password

### LoginDto
- **usernameOrEmail**: Required, not blank
- **password**: Required, not blank

## Configuration

### JWT Settings (application.properties)
```properties
app.jwtSecret=ReplaceThisWithASecureKeyOfAtLeast32Chars!
app.jwtExpirationMs=3600000  # 1 hour in milliseconds
```

### Database Settings
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update  # Auto-create tables
```

## Database Schema

The `users` table will be auto-created with the following structure:

```sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(255) DEFAULT 'ROLE_USER'
);
```

## Security Notes

1. **Change JWT Secret**: Replace the default secret in production
2. **Password Policy**: Currently requires 8+ characters; customize in RegisterDto
3. **Token Expiry**: Default 1 hour; adjust `app.jwtExpirationMs` as needed
4. **HTTPS**: Use HTTPS in production (tokens in headers)
5. **Database Password**: Set a strong password for MySQL root user

## Troubleshooting

### MySQL Connection Issues
- Verify MySQL is running: `Test-NetConnection -ComputerName localhost -Port 3306`
- Check database exists: `mysql -u root -p -e "SHOW DATABASES;"`
- Verify credentials in `application.properties`

### Dialect Errors
- **Already fixed**: Using `MySQLDialect` (not `MySQL8Dialect`)
- If issues persist, Hibernate will log the exact problem

### Port Already in Use
- Change port in `application.properties`: `server.port=8081`

## Next Steps (Optional Enhancements)

1. **Refresh Tokens**: Add refresh token flow for long-lived sessions
2. **Email Verification**: Send verification email on registration
3. **Password Reset**: Implement forgot password flow
4. **Role-based Access**: Add admin/user role checks
5. **Rate Limiting**: Prevent brute force attacks
6. **Swagger/OpenAPI**: Add API documentation
7. **Unit Tests**: Add tests for AuthService and controllers

## Summary

✅ **All components implemented** with the structure you requested:
- ✅ Controller (REST endpoints)
- ✅ Service (Business logic)
- ✅ Repository (Database access)
- ✅ Model/Entity (JPA entities)
- ✅ DTO (Data transfer objects with validation)
- ✅ Config (Security, JWT)
- ✅ Exception (Custom exceptions & global handler)

**The application is ready to run!** Just start it with `.\mvnw.cmd spring-boot:run` and test the endpoints.

