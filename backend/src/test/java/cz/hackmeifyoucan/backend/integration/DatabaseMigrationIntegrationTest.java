package cz.hackmeifyoucan.backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integrační test pro ověření Flyway migrací s využitím Testcontainers.
 *
 * Tento test:
 * 1. Automaticky spustí PostgreSQL container (díky jdbc:tc: v application-test.yaml)
 * 2. Flyway aplikuje všechny migrace z db/migration
 * 3. Test ověří, že tabulky obsahují očekávaná data
 */
@SpringBootTest
@ActiveProfiles("test")
class DatabaseMigrationIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void given_flyway_migrations_when_applied_then_players_table_should_have_seed_data() {
        // Given: Flyway migrace byly aplikovány při startu Spring contextu

        // When: Dotazujeme se na počet hráčů v tabulce
        Integer playerCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM players",
            Integer.class
        );

        // Then: Tabulka by měla obsahovat testovací data z init.sql
        assertThat(playerCount)
            .as("Players table should contain seed data")
            .isNotNull()
            .isGreaterThanOrEqualTo(90);
    }

    @Test
    void given_flyway_migrations_when_applied_then_platform_types_table_should_have_data() {
        // When: Dotazujeme se na počet platform typů
        Integer platformCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM platform_types",
            Integer.class
        );

        // Then: Měly by existovat 2 platformy (email, sms)
        assertThat(platformCount)
            .as("Platform types table should contain at least email and sms")
            .isNotNull()
            .isEqualTo(2);
    }

    @Test
    void given_flyway_migrations_when_applied_then_phishing_categories_table_should_have_data() {
        // When: Dotazujeme se na počet phishing kategorií
        Integer categoryCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM phishing_categories",
            Integer.class
        );

        // Then: Mělo by existovat 7 kategorií (LEGIT, FAKE_URL, URGENT, FAKE_DOC, CRED_THEFT, SPEAR_PHISH, LOTTERY)
        assertThat(categoryCount)
            .as("Phishing categories table should contain all defined categories")
            .isNotNull()
            .isEqualTo(7);
    }

    @Test
    void given_flyway_migrations_when_applied_then_questions_table_should_have_expected_count() {
        // When: Dotazujeme se na počet otázek v tabulce
        Integer questionCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM questions",
            Integer.class
        );

        // Then: Tabulka by měla obsahovat očekávaný počet otázek z V2 migrace
        // Očekáváme 70 otázek podle migračního skriptu (ID 1-70)
        assertThat(questionCount)
            .as("Questions table should contain all seeded questions from V2 migration")
            .isNotNull()
            .isGreaterThanOrEqualTo(70);
    }

    @Test
    void given_flyway_migrations_when_applied_then_question_to_categories_junction_table_should_have_mappings() {
        // When: Dotazujeme se na počet mapování mezi otázkami a kategoriemi
        Integer mappingCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM question_to_categories",
            Integer.class
        );

        // Then: Měly by existovat mapování (každá otázka má alespoň 1 kategorii)
        assertThat(mappingCount)
            .as("Question to categories junction table should have mappings for all questions")
            .isNotNull()
            .isGreaterThanOrEqualTo(70); // Minimálně 70, protože každá otázka má alespoň 1 kategorii
    }

    @Test
    void given_flyway_migrations_when_applied_then_questions_should_reference_valid_platform_types() {
        // When: Dotazujeme se, zda existují otázky s neexistujícím platform_type_id
        Integer invalidReferences = jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*)
            FROM questions q
            WHERE NOT EXISTS (
                SELECT 1 FROM platform_types pt WHERE pt.id = q.platform_type_id
            )
            """,
            Integer.class
        );

        // Then: Všechny otázky by měly mít validní referenci na platform_types
        assertThat(invalidReferences)
            .as("All questions should reference existing platform types")
            .isNotNull()
            .isEqualTo(0);
    }

    @Test
    void given_flyway_migrations_when_applied_then_questions_should_have_required_fields() {
        // When: Dotazujeme se na otázky s prázdnými povinnými poli
        Integer invalidQuestions = jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*)
            FROM questions
            WHERE content IS NULL
               OR content = ''
               OR explanation IS NULL
               OR explanation = ''
               OR platform_type_id IS NULL
            """,
            Integer.class
        );

        // Then: Žádná otázka by neměla mít prázdná povinná pole
        assertThat(invalidQuestions)
            .as("All questions should have required fields filled")
            .isNotNull()
            .isEqualTo(0);
    }

    @Test
    void given_flyway_migrations_when_applied_then_phishing_categories_should_have_unique_tags() {
        // When: Dotazujeme se na duplicitní tagy v phishing_categories
        Integer duplicateTags = jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*)
            FROM (
                SELECT tag
                FROM phishing_categories
                GROUP BY tag
                HAVING COUNT(*) > 1
            ) AS duplicates
            """,
            Integer.class
        );

        // Then: Všechny tagy by měly být unikátní
        assertThat(duplicateTags)
            .as("All phishing category tags should be unique")
            .isNotNull()
            .isEqualTo(0);
    }
}

