# ✅ MYSQL CONNECTION ERROR - COMPLETE FIX

## 🔴 The Problem

```
Communications link failure
Connection refused: getsockopt
```

**Root Cause:** Your MySQL93 service is **STOPPED** and needs to be started.

---

## ✅ QUICK FIX - Start MySQL Service

### **Option 1: Using the Batch File (Easiest)**

I've created a helper script for you:

1. Go to: `C:\Users\dipsh\Downloads\Gym_backend\`
2. Find: `START-MYSQL.bat`
3. **Right-click** → **Run as administrator**
4. MySQL will start automatically

### **Option 2: Manual Command (Run PowerShell as Administrator)**

```powershell
# Right-click PowerShell → Run as Administrator
Start-Service MySQL93
```

### **Option 3: Using Services GUI**

1. Press `Windows + R`
2. Type: `services.msc`
3. Press Enter
4. Find **MySQL93** in the list
5. Right-click → **Start**
6. (Optional) Right-click → **Properties** → Set Startup type to **Automatic**

---

## ✅ Verify MySQL is Running

```powershell
Get-Service MySQL93
```

Expected output:
```
Status   Name      DisplayName
------   ----      -----------
Running  MySQL93   MySQL93
```

---

## ✅ Test Database Connection

```powershell
# Test if port 3306 is accessible
Test-NetConnection -ComputerName localhost -Port 3306
```

Expected: `TcpTestSucceeded : True`

---

## 🚀 Run Your Application

After MySQL is started:

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

## 📋 What I Fixed

1. ✅ **Updated `application.properties`** with better connection settings:
   - Added `createDatabaseIfNotExist=true` - Auto-creates gym database
   - Added connection timeout settings
   - Added timezone configuration
   
2. ✅ **Created `START-MYSQL.bat`** - Helper script to start MySQL easily

3. ✅ **Identified the issue** - MySQL93 service is stopped

---

## 🎯 Complete Startup Checklist

**Before running your Spring Boot app:**

1. [ ] Start MySQL service (use `START-MYSQL.bat` as admin)
2. [ ] Verify MySQL is running: `Get-Service MySQL93`
3. [ ] Check port 3306 is open: `Test-NetConnection -ComputerName localhost -Port 3306`
4. [ ] Update password in `application.properties` if your root user has a password
5. [ ] Run application: `.\mvnw.cmd spring-boot:run`

---

## ⚙️ Set MySQL to Start Automatically (Optional)

To avoid this error in the future:

```powershell
# Run as Administrator
Set-Service MySQL93 -StartupType Automatic
Start-Service MySQL93
```

Or use Services GUI:
1. `services.msc` → Find MySQL93
2. Right-click → Properties
3. Startup type: **Automatic**
4. Click **Apply** → **Start**

---

## 🔧 If You Still Get Connection Errors

### Check Your Password

If you see "Access denied":

**Edit:** `src/main/resources/application.properties`

Line 6:
```properties
spring.datasource.password=YOUR_MYSQL_ROOT_PASSWORD
```

### Check Your Port

If MySQL is on a different port:

```sql
-- Connect to MySQL
mysql -u root -p

-- Check port
SHOW VARIABLES LIKE 'port';
```

If not 3306, update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:YOUR_PORT/gym?createDatabaseIfNotExist=true&useSSL=false
```

---

## ✅ Summary

**The Issue:**
- ❌ MySQL93 service was **STOPPED**
- ❌ Application couldn't connect to database

**The Fix:**
1. ✅ Start MySQL service: Use `START-MYSQL.bat` (run as admin)
2. ✅ Verify it's running: `Get-Service MySQL93`
3. ✅ Run your app: `.\mvnw.cmd spring-boot:run`

**Files Created:**
- `START-MYSQL.bat` - Helper script to start MySQL
- `MYSQL-CONNECTION-FIX.md` - Detailed troubleshooting guide
- Updated `application.properties` with better connection settings

---

## 🚀 Next Steps

1. **Right-click `START-MYSQL.bat` → Run as administrator**
2. Wait for "MySQL93 service started successfully!"
3. Run: `.\mvnw.cmd spring-boot:run`
4. Your backend will start on `http://localhost:8080`
5. Your frontend at `http://localhost:3000` can now connect!

---

**Start MySQL now and run your application!** 🎉

