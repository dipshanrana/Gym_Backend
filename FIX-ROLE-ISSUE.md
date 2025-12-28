# 🔴 FIX: "Access denied. Admin privileges required. (Current role: none)"

## 🎯 Quick Diagnosis

This error means your frontend is getting `undefined` or `null` for the role field.

---

## ✅ SOLUTION - Step by Step

### Step 1: Run Diagnostic Script

```bash
FIX-ADMIN-ROLE.bat
```

This will:
1. Test your login endpoint
2. Show you the actual response
3. Tell you what's wrong

### Step 2: Check the Response

The response should look like this:

**✅ CORRECT:**
```json
{
  "token": "eyJ...",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@fitfuel.com",
    "role": "ROLE_ADMIN",  ← MUST BE PRESENT!
    "passwordChangeRequired": true
  }
}
```

**❌ WRONG (Causes your error):**
```json
{
  "token": "eyJ...",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@fitfuel.com",
    "role": null,  ← NULL or missing!
    "passwordChangeRequired": true
  }
}
```

---

## 🔧 Common Fixes

### Fix 1: Backend Not Restarted (Most Common!)

**Problem:** Code changes not applied because backend is still running old code.

**Solution:**
```bash
# 1. Stop current backend (Ctrl+C in terminal)
# 2. Restart
RUN.bat
```

**Wait for:** "Started GymApplication in X seconds"

**Then test:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"Admin@123"}'
```

---

### Fix 2: Database Missing Column

**Problem:** `password_change_required` column doesn't exist in database.

**Check:**
```sql
USE gym;
DESCRIBE users;
```

**If missing, add it:**
```sql
ALTER TABLE users ADD COLUMN password_change_required BOOLEAN DEFAULT FALSE;
```

**OR** just restart backend (JPA will auto-create it if `ddl-auto=update`)

---

### Fix 3: Admin User Has No Role

**Problem:** Admin exists but `role` field is NULL.

**Check:**
```sql
SELECT username, role FROM users WHERE username = 'admin';
```

**Fix:**
```sql
UPDATE users SET role = 'ROLE_ADMIN' WHERE username = 'admin';
```

---

### Fix 4: Recreate Admin User

**Problem:** Admin was created before our code changes.

**Solution:**
```sql
USE gym;
DELETE FROM users WHERE username = 'admin';
```

**Then restart backend:**
```bash
RUN.bat
```

Admin will be recreated with all correct fields.

---

## 🧪 Testing After Fix

### Test 1: Backend Test
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"Admin@123"}'
```

**Must see:**
```json
{
  "user": {
    "role": "ROLE_ADMIN"  ← Must be present!
  }
}
```

### Test 2: Frontend Test
```javascript
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    usernameOrEmail: 'admin',
    password: 'Admin@123'
  })
});

const data = await response.json();
console.log('Full response:', data);
console.log('User:', data.user);
console.log('Role:', data.user.role);  // ← Should be "ROLE_ADMIN"
```

---

## 🔍 Detailed Diagnosis

### Check 1: Is Backend Running?
```bash
curl http://localhost:8080/api/auth/login
```
- If connection refused → Backend not running
- If returns 400 → Backend running (expected error without credentials)

### Check 2: Is Database Connected?
Look at backend console for:
```
Hibernate: select ...
```
If you see SQL queries → Database connected ✅

### Check 3: Was Admin Created?
Look for this in backend console:
```
✅ ADMIN USER CREATED SUCCESSFULLY!
📧 Email:    admin@fitfuel.com
👤 Username: admin
```

Or:
```
✅ Admin user 'admin' already exists
```

### Check 4: Check Database Directly
```sql
USE gym;
SELECT * FROM users WHERE username = 'admin';
```

Should show:
```
id | username | email              | role       | password_change_required
1  | admin    | admin@fitfuel.com  | ROLE_ADMIN | 1
```

---

## 🎯 Most Likely Solution

**99% of the time, the issue is:**

### **Backend needs to be restarted!**

```bash
# Stop backend (Ctrl+C)
RUN.bat
```

That's it! Our code changes require a restart to take effect.

---

## 🚨 Emergency Fix (If Nothing Works)

If none of the above works, do a complete reset:

### Step 1: Clean Database
```sql
USE gym;
DROP TABLE IF EXISTS users;
```

### Step 2: Clean Backend Build
```bash
.\mvnw.cmd clean
```

### Step 3: Restart Backend
```bash
RUN.bat
```

### Step 4: Test
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"Admin@123"}'
```

Should see complete response with role.

---

## ✅ Verification Checklist

- [ ] Backend restarted
- [ ] MySQL running
- [ ] Console shows admin created
- [ ] cURL test returns role in response
- [ ] Database has admin with ROLE_ADMIN
- [ ] Frontend can access data.user.role
- [ ] No "role: none" error

---

## 📞 Still Not Working?

### Run These Commands:

```bash
# 1. Test backend is running
curl http://localhost:8080/api/auth/login

# 2. Test login returns role
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"Admin@123"}' | jq .user.role

# 3. Check database
mysql -u root -e "USE gym; SELECT username, role FROM users WHERE username='admin';"
```

### Check Logs:

Look for errors in:
1. Backend console
2. Browser console (F12)
3. Browser Network tab (check actual response)

---

## 🎊 Summary

**Most Common Issue:** Backend not restarted after code changes

**Quick Fix:** 
```bash
RUN.bat  # That's it!
```

**Verify:**
```bash
FIX-ADMIN-ROLE.bat  # Run this to test
```

**Expected:** Response includes `"role": "ROLE_ADMIN"`

---

**Your issue will be fixed after restarting the backend! 🚀**

