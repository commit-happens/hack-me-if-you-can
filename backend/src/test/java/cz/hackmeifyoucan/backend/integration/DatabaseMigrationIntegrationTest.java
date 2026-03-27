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
    void given_flyway_migrations_when_applied_then_questions_should_use_only_supported_platform_type_ids() {
        // When: Dotazujeme se na otázky s nepodporovaným platform_type_id
        Integer invalidPlatformTypeIds = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM questions WHERE platform_type_id NOT IN (1, 2)",
            Integer.class
        );

        // Then: Platform type musí odpovídat enum hodnotám EMAIL=1 a SMS=2
        assertThat(invalidPlatformTypeIds)
            .as("Questions should use only enum platform type ids (1=email, 2=sms)")
            .isNotNull()
            .isEqualTo(0);
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

    @Test
    void given_flyway_migrations_when_applied_then_phishing_categories_should_have_reward_points() {
        Integer missingRewardPoints = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM phishing_categories WHERE reward_points IS NULL OR reward_points <= 0",
            Integer.class
        );

        assertThat(missingRewardPoints)
            .as("All phishing categories should have positive reward_points")
            .isNotNull()
            .isEqualTo(0);
    }
    @Test
    void given_player_insert_without_score_when_inserting_then_default_score_should_be_200() {
        jdbcTemplate.update("DELETE FROM players WHERE nickname = ?", TEMP_PLAYER_NICK);
        Integer createdScore = jdbcTemplate.queryForObject(
            "INSERT INTO players (nickname) VALUES (?) RETURNING score",
            Integer.class,
            TEMP_PLAYER_NICK
        );

        assertThat(createdScore)
            .as("Players default score should be 200")
            .isEqualTo(200);
    }
}

