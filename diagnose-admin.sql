-- ========================================
-- DIAGNOSTIC SQL - Check Admin User Status
-- ========================================
-- Run this in MySQL to diagnose the issue

USE gym;

-- 1. Check if users table exists and its structure
DESCRIBE users;

-- 2. Check if admin user exists
SELECT * FROM users WHERE username = 'admin';

-- 3. Check all users and their roles
SELECT id, username, email, role, enabled, password_change_required
FROM users;

-- 4. Count users by role
SELECT role, COUNT(*) as count
FROM users
GROUP BY role;

-- ========================================
-- FIXES (if needed)
-- ========================================

-- Fix 1: If password_change_required column doesn't exist
-- ALTER TABLE users ADD COLUMN password_change_required BOOLEAN DEFAULT FALSE;

-- Fix 2: If admin doesn't exist, delete and let app recreate
-- DELETE FROM users WHERE username = 'admin';
-- Then restart your Spring Boot application

-- Fix 3: If admin exists but has wrong role
-- UPDATE users SET role = 'ROLE_ADMIN' WHERE username = 'admin';

-- Fix 4: If admin exists but passwordChangeRequired is NULL
-- UPDATE users SET password_change_required = true WHERE username = 'admin' AND role = 'ROLE_ADMIN';

-- ========================================
-- VERIFICATION
-- ========================================
-- After fixes, admin should look like this:
-- id | username | email              | role       | enabled | password_change_required
-- 1  | admin    | admin@fitfuel.com  | ROLE_ADMIN | 1       | 1

