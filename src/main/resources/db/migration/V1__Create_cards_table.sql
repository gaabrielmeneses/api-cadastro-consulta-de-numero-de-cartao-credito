CREATE TABLE cards (
    id BINARY(16) NOT NULL PRIMARY KEY,
    encrypted_card_number VARCHAR(500) NOT NULL,
    card_hash VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_card_hash (card_hash)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;