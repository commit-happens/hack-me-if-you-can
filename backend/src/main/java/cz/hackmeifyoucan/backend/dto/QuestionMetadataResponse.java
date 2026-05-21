package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "QuestionMetadataResponse",
        description = "Metadata otázky obsahující odesílatele a předmět",
        example = "{\"sender\": \"security@acme.com\", \"subject\": \"Urgent account verification\"}"
)
public record QuestionMetadataResponse(
        @Schema(
                description = "Odesílatel zprávy (typicky emailová adresa) nebo jiná relevantní informace pro danou platformu",
                example = "security@acme.com"
        )
        String sender,

        @Schema(
                description = "Předmět zprávy (typicky pro emaily) nebo jiná relevantní informace pro danou platformu",
                example = "Urgent account verification"
        )
        String subject
) {
}