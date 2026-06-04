package cz.hackmeifyoucan.backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Typ platformy na kterou je otázka cílená")
public enum PlatformType {
    EMAIL(1, "EMAIL"),
    SMS(2, "SMS");

    private final int id;
    private final String apiValue;

    PlatformType(int id, String apiValue) {
        this.id = id;
        this.apiValue = apiValue;
    }

    public String getName() {
        return apiValue.toLowerCase();
    }

    public static PlatformType fromId(int id) {
        for (PlatformType platformType : values()) {
            if (platformType.id == id) {
                return platformType;
            }
        }
        throw new IllegalArgumentException("Unsupported platform type id: " + id);
    }

    public static PlatformType fromName(String name) {
        for (PlatformType platformType : values()) {
            if (platformType.getName().equalsIgnoreCase(name)) {
                return platformType;
            }
        }
        throw new IllegalArgumentException("Unsupported platform type: " + name + ". Allowed values: email, sms");
    }
}
