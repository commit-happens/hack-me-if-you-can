-- Flyway migrace V1: Vytvoření tabulky hráčů
-- Tato migrace vytváří počáteční schéma pro tabulku hráčů

CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    score INT NOT NULL
);
