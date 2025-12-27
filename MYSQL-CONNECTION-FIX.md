# 🔴 MySQL Connection Error - FIXED

## The Error You're Seeing

```
Communications link failure
Connection refused: getsockopt
```

This means your **MySQL server is NOT running** or not accessible on `localhost:3306`.

---

## ✅ SOLUTION: Start MySQL Service

### Option 1: Start MySQL Service (Windows)

**Method A: Using Services**
1. Press `Windows + R`
2. Type `services.msc` and press Enter
3. Find **MySQL** or **MySQL80** in the list
4. Right-click → **Start**
5. Change Startup Type to **Automatic** (so it starts with Windows)

**Method B: Using Command Prompt (Run as Administrator)**
```cmd
net start MySQL80
```
or
```cmd
net start MySQL
```

**Method C: Using PowerShell (Run as Administrator)**
```powershell
Start-Service MySQL80
```

### Option 2: Check if MySQL is Installed

If MySQL is not installed, you need to install it first:

**Download & Install:**
1. Go to: https://dev.mysql.com/downloads/installer/
2. Download **MySQL Installer for Windows**
3. Install **MySQL Server** (select default settings)
4. Remember the **root password** you set during installation
5. Update `application.properties` with your password

---

## ✅ Verify MySQL is Running

### Check MySQL Service Status

**PowerShell:**
```powershell
Get-Service MySQL80
```

Expected output:
```
Status   Name               DisplayName
------   ----               -----------
Running  MySQL80            MySQL80
```

### Check if Port 3306 is Open

```powershell
Test-NetConnection -ComputerName localhost -Port 3306
```

Expected output:
```
TcpTestSucceeded : True
```

### Test MySQL Connection

```powershell
# Connect to MySQL (requires MySQL client installed)
mysql -u root -p
```

If successful, you'll see:
```
mysql>
```

Then verify the database:
```sql
SHOW DATABASES;
CREATE DATABASE IF NOT EXISTS gym;
EXIT;
```

---

## ✅ Update Your Password

If your MySQL root user has a password, update `application.properties`:

**File:** `src/main/resources/application.properties`

Change line 6:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

---

## ✅ What I Fixed in Configuration

Updated `application.properties` with better connection settings:

1. ✅ Added `createDatabaseIfNotExist=true` - Auto-creates `gym` database
2. ✅ Added `serverTimezone=UTC` - Fixes timezone issues
3. ✅ Added connection timeout settings
4. ✅ Added connection pool configuration

---

## 🚀 Start Your Application (After MySQL is Running)

```powershell
cd C:\Users\dipsh\Downloads\Gym_backend
.\mvnw.cmd spring-boot:run
```

Expected output:
```
Started GymApplication in X.XXX seconds
Tomcat started on port 8080
```

---

## 🔍 Common MySQL Issues & Fixes

### Issue 1: MySQL Service Won't Start

**Fix:**
```cmd
# Run as Administrator
net stop MySQL80
net start MySQL80
```

### Issue 2: Wrong Port

Check which port MySQL is using:
```sql
SHOW VARIABLES LIKE 'port';
```

If not 3306, update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:YOUR_PORT/gym?createDatabaseIfNotExist=true
```

### Issue 3: Access Denied

**Fix:** Update password in `application.properties` or reset MySQL password:

**Reset MySQL Password (Windows):**
1. Stop MySQL service
2. Create `my.ini` with:
   ```ini
   [mysqld]
   skip-grant-tables
   ```
3. Start MySQL in safe mode
4. Connect and reset password:
   ```sql
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';
   FLUSH PRIVILEGES;
   ```
5. Restart MySQL normally

### Issue 4: Firewall Blocking

**Fix:** Allow MySQL through Windows Firewall:
```powershell
# Run as Administrator
New-NetFirewallRule -DisplayName "MySQL" -Direction Inbound -Protocol TCP -LocalPort 3306 -Action Allow
```

---

## ✅ Quick Checklist

Before running the application, ensure:

- [ ] MySQL service is **RUNNING** (`Get-Service MySQL80`)
- [ ] Port **3306** is accessible (`Test-NetConnection -ComputerName localhost -Port 3306`)
- [ ] Database **gym** exists (or will be auto-created)
- [ ] Password in `application.properties` matches MySQL root password
- [ ] No firewall blocking port 3306

---

## 🎯 Alternative: Use Different Database Port

If MySQL is running on a different port (e.g., 3307):

**Update `application.properties`:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3307/gym?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

---

## 📊 Verify Everything Works

```powershell
# 1. Check MySQL is running
Get-Service MySQL80

# 2. Test connection
mysql -u root -p

# 3. In MySQL, create database
CREATE DATABASE IF NOT EXISTS gym;
SHOW DATABASES;
EXIT;

# 4. Run Spring Boot application
cd C:\Users\dipsh\Downloads\Gym_backend
.\mvnw.cmd spring-boot:run
```

---

## ✅ Summary

**The Problem:**
- ❌ MySQL server is not running
- ❌ Cannot connect to `localhost:3306`

**The Solution:**
1. ✅ Start MySQL service: `net start MySQL80`
2. ✅ Verify service is running: `Get-Service MySQL80`
3. ✅ Update password in `application.properties` if needed
4. ✅ Run your application: `.\mvnw.cmd spring-boot:run`

**After fixing MySQL, your application will:**
- ✅ Connect to MySQL database
- ✅ Auto-create `gym` database
- ✅ Create `users` and `demo` tables
- ✅ Start on port 8080
- ✅ Accept login/register requests from frontend

---

**Start MySQL service now and run the application again!** 🚀

