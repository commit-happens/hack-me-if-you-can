package cz.hackmeifyoucan.backend.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Typ platformy na kterou je otázka cílená")
public enum PlatformType {
    EMAIL(1, "email"),
    SMS(2, "sms");

    private final int id;
    private final String apiValue;

    PlatformType(int id, String apiValue) {
        this.id = id;
        this.apiValue = apiValue;
    }

    @JsonValue
    public String getName() {
        return apiValue;
    }

    public static PlatformType fromId(int id) {
        for (PlatformType platformType : values()) {
            if (platformType.id == id) {
                return platformType;
            }
        }
        throw new IllegalArgumentException("Unsupported platform type id: " + id);
    }
}

