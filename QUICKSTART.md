# Quick Start Guide

## Run the Application

```powershell
# Navigate to project directory
cd C:\Users\dipsh\Downloads\Gym_backend

# Run the application
.\mvnw.cmd spring-boot:run
```

## Prerequisites

1. **MySQL Database** running on `localhost:3306`
2. **Database Created**: 
   ```sql
   CREATE DATABASE gym;
   ```
3. **Password Set**: Update `spring.datasource.password` in `src/main/resources/application.properties` if your MySQL root user has a password

## Test Endpoints

### 1. Register
```powershell
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"john\",\"email\":\"john@gym.com\",\"password\":\"Test1234\",\"confirmPassword\":\"Test1234\"}"
```

### 2. Login
```powershell
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"usernameOrEmail\":\"john\",\"password\":\"Test1234\"}"
```

### 3. Access Protected Route
```powershell
# Copy token from login response and use it here
curl -H "Authorization: Bearer YOUR_TOKEN_HERE" http://localhost:8080/api/test/hello
```

That's it! Your authentication system is ready to use. 🚀

