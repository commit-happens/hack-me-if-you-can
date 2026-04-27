package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AnswerId implements Serializable {

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "session_id")
    private String sessionId;
}


