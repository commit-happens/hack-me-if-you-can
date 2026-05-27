package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_problems")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class QuestionProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_questions_to_problems_question_id"))
    private Question question;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_questions_to_problems_problem_id"))
    private Problem problem;
}


