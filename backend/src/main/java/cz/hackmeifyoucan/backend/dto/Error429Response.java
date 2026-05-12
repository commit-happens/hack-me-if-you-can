package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "429 Too Many Requests - překročen limit AI služby")
public record Error429Response(
    @Schema(description = "HTTP status kód", example = "429")
    Integer status,

    @Schema(description = "Popis chyby", example = "Byl překročen limit volání AI služby, zkuste to prosím znovu později")
    String error
) {}
