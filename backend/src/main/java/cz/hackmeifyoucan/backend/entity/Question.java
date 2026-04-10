package cz.hackmeifyoucan.backend.entity;

import cz.hackmeifyoucan.backend.converter.DifficultyConverter;
import cz.hackmeifyoucan.backend.converter.PlatformTypeConverter;
import cz.hackmeifyoucan.backend.enums.Difficulty;
import cz.hackmeifyoucan.backend.enums.PlatformType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString(exclude = "categories")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "platform_type_id", nullable = false)
    @Convert(converter = PlatformTypeConverter.class)
    private PlatformType platformType;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "question_to_categories",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<PhishingCategory> categories = new HashSet<>();

    @Column(name = "is_phishing", nullable = false)
    private boolean phishing;

    @Column(name = "difficulty", nullable = false)
    @Convert(converter = DifficultyConverter.class)
    private Difficulty difficulty;

    @Column(name = "penalty", nullable = false)
    private int penalty;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, String> metadata = new HashMap<>();

    @Column(name = "explanation", nullable = false, length = 1000)
    private String explanation;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
