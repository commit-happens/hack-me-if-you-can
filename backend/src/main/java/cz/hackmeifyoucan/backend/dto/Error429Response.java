package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "429 Too Many Requests - překročen limit AI služby")
public class Error429Response {

    @Schema(description = "HTTP status kód", example = "429")
    private Integer status;

    @Schema(description = "Popis chyby", example = "Byl překročen limit volání AI služby, zkuste to prosím znovu později")
    private String error;
}

