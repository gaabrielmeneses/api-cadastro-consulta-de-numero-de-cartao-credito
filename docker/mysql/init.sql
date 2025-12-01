-- Initialize MySQL database for Credit Card API
CREATE DATABASE IF NOT EXISTS creditcard_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Grant privileges to application user
GRANT ALL PRIVILEGES ON creditcard_db.* TO 'creditcard_user'@'%';
FLUSH PRIVILEGES;