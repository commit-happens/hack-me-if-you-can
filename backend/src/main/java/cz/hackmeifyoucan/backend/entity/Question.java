package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_type_id", nullable = false)
    private PlatformType platformType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "question_to_categories",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<PhishingCategory> categories;

    @Column(name = "is_phishing", nullable = false)
    private boolean phishing;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;

    @Column(name = "penalty", nullable = false)
    private Integer penalty;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, String> metadata = new HashMap<>();

    @Column(name = "explanation", nullable = false, length = 1000)
    private String explanation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
