package cz.hackmeifyoucan.backend.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QuestionProblemId implements Serializable {

    private Long questionId;
    private Long problemId;
}