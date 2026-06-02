-- Odstraň sloupec penalty z tabulky questions, jelikož není již potřebný
ALTER TABLE questions
DROP COLUMN penalty;

