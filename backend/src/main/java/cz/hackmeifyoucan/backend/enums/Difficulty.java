package cz.hackmeifyoucan.backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Úroveň obtížnosti otázky")
public enum Difficulty {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int level;

    Difficulty(int level) {
        this.level = level;
    }

}


