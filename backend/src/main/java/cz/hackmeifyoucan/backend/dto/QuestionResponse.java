package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "QuestionResponse",
        description = "Reprezentuje jednu phishingovou otázku vracenou API",
        example = "{\"id\": 35, \"platform\": \"email\", \"metadata\": {\"sender\": \"security@acme.com\", \"subject\": \"Urgent account verification\"}, \"content\": \"Please verify your account immediately\", \"explanation\": \"Podvodný email tlací na rychlou akci\"}"
)
public record QuestionResponse (
        @Schema(
                description = "Unikátní identifikátor otázky",
                example = "35"
        )
        Long id,

        @Schema(
                description = "Platforma, na kterou se otázka vztahuje (např. email, sms, web)",
                example = "email"
        )
        String platform,

        @Schema(
                description = "Metadata otázky obsahující dodatečné informace (odesílatel, předmět, atd.)",
                implementation = QuestionMetadataResponse.class
        )
        QuestionMetadataResponse metadata,

        @Schema(
                description = "Obsah otázky - text nebo popis podezřelé zprávy",
                example = "Please verify your account immediately"
        )
        String content,

        @Schema(
                description = "Vysvětlení, proč se jedná o phishing a jaké jsou varovné znaky",
                example = "Podvodný email tlačí na rychlou akci"
        )
        String explanation,

        @Schema(
                description = "Seznam problémů (chytáků) identifikovaných v obsahu zprávy",
                example = "[{\"id\": \"time-pressure\", \"description\": \"Uměle vytvářený tlak časovým limitem...\"}]"
        )
        List<Map<String, String>> problems
) {
}