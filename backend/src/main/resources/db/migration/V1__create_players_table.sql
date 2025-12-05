-- Flyway migration V1: Create players table
-- This migration creates the initial schema for the players table

CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    score INT NOT NULL
);
