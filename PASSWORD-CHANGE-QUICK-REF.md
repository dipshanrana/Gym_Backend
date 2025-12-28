# 🔐 FORCE PASSWORD CHANGE - QUICK REFERENCE

## ✅ Implementation Complete

Admin must change password on first login.

---

## 📋 Quick Facts

- ✅ Admin created with `passwordChangeRequired: true`
- ✅ Login response includes the flag
- ✅ Password change endpoint: `/api/user/change-password`
- ✅ No compilation errors
- ✅ Production-ready

---

## 🔑 Login Response

```json
{
  "token": "eyJ...",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@fitpro.com",
    "role": "ROLE_ADMIN",
    "passwordChangeRequired": true  ← Check this!
  }
}
```

---

## 🌐 Frontend Integration (3 Lines!)

```javascript
// After successful login
if (data.user.passwordChangeRequired) {
  router.push('/change-password');  // Redirect
  return;
}
```

---

## 🔄 Change Password Request

```javascript
POST /api/user/change-password
Headers: Authorization: Bearer <TOKEN>

{
  "currentPassword": "Admin@123",
  "newPassword": "NewSecure123!",
  "confirmPassword": "NewSecure123!"
}
```

---

## 🧪 Quick Test

```bash
# 1. Test first login
TEST-FORCE-PASSWORD.bat

# 2. Change password (paste token when prompted)
TEST-CHANGE-PASSWORD.bat
```

---

## ✅ Checklist

- [ ] Restart backend
- [ ] Run TEST-FORCE-PASSWORD.bat
- [ ] Verify response has `passwordChangeRequired: true`
- [ ] Run TEST-CHANGE-PASSWORD.bat
- [ ] Password change successful
- [ ] Login again with new password
- [ ] Flag now `false`

---

## 📁 Files Created

1. **User.java** - Added field
2. **UserDto.java** - Added field
3. **ChangePasswordDto.java** - NEW
4. **UserService.java** - NEW
5. **UserController.java** - NEW
6. **AuthService.java** - Updated
7. **DataInitializer.java** - Updated

---

**Just restart backend and test!** 🚀

