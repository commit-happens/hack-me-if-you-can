package cz.hackmeifyoucan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "401 Unauthorized - neautorizovaný interní přístup")
public class Error401Response {

    @Schema(description = "HTTP status kód", example = "401")
    private Integer status;

    @Schema(description = "Krátký popis HTTP chyby", example = "Unauthorized")
    private String error;

    @Schema(description = "Detail chyby", example = "Neautorizovaný interní přístup")
    private String message;

    @Schema(description = "Volaná cesta", example = "/api/admin/questions/email")
    private String path;
}

