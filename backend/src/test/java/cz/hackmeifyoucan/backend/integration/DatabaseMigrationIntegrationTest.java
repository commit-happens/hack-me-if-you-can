package cz.hackmeifyoucan.backend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseMigrationIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void given_flyway_migrations_when_applied_then_players_table_should_have_seed_data() {
        Integer playerCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM players",
            Integer.class
        );

        assertThat(playerCount)
            .as("Players table should contain seed data")
            .isNotNull()
            .isGreaterThanOrEqualTo(90);
    }

    @Test
    void given_flyway_migrations_when_applied_then_questions_should_use_only_supported_platform_type_ids() {
        Integer invalidPlatformTypeIds = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM questions WHERE platform_type_id NOT IN (1, 2)",
            Integer.class
        );

        assertThat(invalidPlatformTypeIds)
            .as("Questions should use only enum platform type ids (1=email, 2=sms)")
            .isNotNull()
            .isEqualTo(0);
    }

    @Test
    void given_flyway_migrations_when_applied_then_phishing_categories_table_should_have_data() {
        Integer categoryCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM phishing_categories",
            Integer.class
        );

        assertThat(categoryCount)
            .as("Phishing categories table should contain all defined categories")
            .isNotNull()
            .isEqualTo(7);
    }


    @Test
    void given_flyway_migrations_when_applied_then_questions_should_reference_existing_category_id() {
        Integer invalidCategoryRefs = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM questions q
                LEFT JOIN phishing_categories c ON c.id = q.phishing_category_id
                WHERE q.phishing_category_id IS NULL OR c.id IS NULL
                """,
                Integer.class
        );

        assertThat(invalidCategoryRefs)
                .as("Every question should reference one valid phishing_category_id")
                .isNotNull()
                .isEqualTo(0);
    }

    @Test
    void given_flyway_migrations_when_applied_then_question_to_categories_junction_table_should_not_exist() {
        Integer tableExists = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_schema = 'public' AND table_name = 'question_to_categories'
                """,
                Integer.class
        );

        assertThat(tableExists)
                .as("question_to_categories junction table should be removed")
                .isNotNull()
                .isEqualTo(0);
    }


    @Test
    void given_flyway_migrations_when_applied_then_questions_should_have_required_fields() {
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

        assertThat(invalidQuestions)
            .as("All questions should have required fields filled")
            .isNotNull()
            .isEqualTo(0);
    }

    @Test
    void given_flyway_migrations_when_applied_then_phishing_categories_should_have_unique_tags() {
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
    void given_flyway_migrations_when_applied_then_answers_table_should_exist() {
        Integer tableExists = jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*)
            FROM information_schema.tables
            WHERE table_schema = 'public' AND table_name = 'answers'
            """,
            Integer.class
        );

        assertThat(tableExists)
            .as("Answers table should exist")
            .isNotNull()
            .isEqualTo(1);
    }

}

