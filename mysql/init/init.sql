-- Initialize MySQL database for Credit Card API
CREATE DATABASE IF NOT EXISTS creditcard CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE creditcard;

-- Create cards table
CREATE TABLE cards (
    id BINARY(16) NOT NULL PRIMARY KEY,
    encrypted_card_number VARCHAR(500) NOT NULL,
    card_hash VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_card_hash ON cards(card_hash);

-- Create users table
CREATE TABLE users (
    id BINARY(16) NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(60) NOT NULL
);

CREATE UNIQUE INDEX idx_username ON users(username);

-- Grant privileges to application user
GRANT ALL PRIVILEGES ON creditcard.* TO 'creditcard_user'@'%';
FLUSH PRIVILEGES;
