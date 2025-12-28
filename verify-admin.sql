-- Test Admin User Creation
-- Run this after starting your application to verify admin was created

USE gym;

-- Check if admin user exists
SELECT * FROM users WHERE role = 'ROLE_ADMIN';

-- Check if admin user exists by username
SELECT * FROM users WHERE username = 'admin';

-- Check if admin user exists by email
SELECT * FROM users WHERE email = 'admin@fitfuel.com';

-- View all users
SELECT id, username, email, fullname, role, enabled FROM users;

-- Count users by role
SELECT role, COUNT(*) as count FROM users GROUP BY role;

