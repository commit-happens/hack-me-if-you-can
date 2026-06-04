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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
@Getter
@Setter
@ToString(exclude = "phishingCategory")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phishing_category_id", nullable = false)
    private PhishingCategory phishingCategory;

    @Column(name = "is_phishing", nullable = false)
    private boolean phishing;

    @Column(name = "difficulty", nullable = false)
    @Convert(converter = DifficultyConverter.class)
    private Difficulty difficulty;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, String> metadata = new HashMap<>();

    @Column(name = "explanation", nullable = false, length = 2000)
    private String explanation;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "question_problems",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "problem_id")
    )
    private List<Problem> problems = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
