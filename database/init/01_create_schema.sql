-- Database initialization script for Hack Me If You Can
-- This script runs automatically when Docker container starts
--
-- WHAT THIS DOES:
-- 1. Creates the database schema
-- 2. Creates tables with proper structure
-- 3. Inserts sample data for testing
-- 4. Sets up indexes for better performance
--
-- FILE NAMING: Docker executes files in alphabetical order
-- Use 01_, 02_, etc. to control execution sequence

-- Ensure we're using the correct database
USE hackme_db;

-- Create players table with proper constraints
CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    score INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Indexes for better performance
    INDEX idx_nickname (nickname),
    INDEX idx_score (score),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create game_sessions table (future enhancement)
CREATE TABLE IF NOT EXISTS game_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    session_start TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    session_end TIMESTAMP NULL,
    questions_answered INT DEFAULT 0,
    correct_answers INT DEFAULT 0,
    final_score INT DEFAULT 0,
    
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    INDEX idx_player_id (player_id),
    INDEX idx_session_start (session_start)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create phishing_questions table (future enhancement)  
CREATE TABLE IF NOT EXISTS phishing_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_text TEXT NOT NULL,
    question_type VARCHAR(50) NOT NULL,
    difficulty_level ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    explanation TEXT,
    points INT NOT NULL DEFAULT 10,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_difficulty (difficulty_level),
    INDEX idx_is_active (is_active),
    INDEX idx_question_type (question_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data for testing
INSERT IGNORE INTO players (nickname, score) VALUES
('demo_player', 100),
('test_user', 75),
('phishing_expert', 250),
('beginner_joe', 25),
('security_guru', 500);

-- Insert sample questions for testing
INSERT IGNORE INTO phishing_questions (question_text, question_type, difficulty_level, correct_answer, explanation, points) VALUES
('Is this email from your bank legitimate?', 'EMAIL_ANALYSIS', 'EASY', 'NO', 'Banks never ask for passwords via email', 10),
('What should you do when you receive a suspicious link?', 'BEST_PRACTICE', 'MEDIUM', 'REPORT_AND_DELETE', 'Always report suspicious links to IT security', 15),
('How can you verify if a website is secure?', 'TECHNICAL', 'HARD', 'CHECK_SSL_CERTIFICATE', 'Look for HTTPS and valid SSL certificates', 25);

-- Create database user for application (if not exists)
-- Note: This might not work in all Docker setups due to permissions
-- CREATE USER IF NOT EXISTS 'hackme_app'@'%' IDENTIFIED BY 'app_password123';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON hackme_db.* TO 'hackme_app'@'%';
-- FLUSH PRIVILEGES;

-- Show table information
SHOW TABLES;
DESCRIBE players;
DESCRIBE game_sessions;  
DESCRIBE phishing_questions;