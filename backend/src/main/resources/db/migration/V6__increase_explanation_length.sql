-- Zvětšení délky sloupce `explanation` z 1000 na 2000 znaků
ALTER TABLE questions ALTER COLUMN explanation TYPE VARCHAR(2000);

