CREATE TABLE parents (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(20),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    address TEXT NOT NULL
);

-- 1. Coaches Profile Table
CREATE TABLE coaches (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    specialization VARCHAR(100), -- e.g., Tactics, Goalkeeping, Fitness
    license_level VARCHAR(50)    -- e.g., UEFA A, USSF B
);

-- 2. Players Profile Table
CREATE TABLE players (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE, -- Optional for minor players
    date_of_birth DATE NOT NULL,
    jersey_number INT,
    position VARCHAR(30),       -- e.g., Striker, Midfielder, Defender
    parent_id BIGINT REFERENCES parents(user_id) ON DELETE SET NULL -- Links Player to their Parent
);

-- 3. Admins Profile Table
CREATE TABLE admins (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    employee_id VARCHAR(30) UNIQUE NOT NULL
);

ALTER TABLE players ADD COLUMN passport_url VARCHAR(500);

CREATE TABLE player_profiles (
    player_id BIGINT PRIMARY KEY REFERENCES players(id) ON DELETE CASCADE,
    height_cm DECIMAL(5,2) NOT NULL,
    weight_kg DECIMAL(5,2) NOT NULL,
    dominant_foot VARCHAR(10) NOT NULL, -- "LEFT", "RIGHT", "AMBIDEXTROUS"
    position VARCHAR(30) NOT NULL,      -- "Striker", "Goalkeeper", etc.
    preferred_jersey_number INT,
    biography TEXT
);