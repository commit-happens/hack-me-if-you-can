/* Entita Player mapovaná do databáze (obsahuje informace o hráči). */

package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Player {

	// ID je generované databází; označíme ho jako read-only pro Swagger a JSON deserializaci
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Auto-generated player id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long playerId;
    
    @Column(nullable = false, unique = true)
	private String nickname;

	@Schema(defaultValue = "100", example = "100", description = "Player score")
	@JsonProperty(defaultValue = "100")
	@Builder.Default
	private Integer score = 100;


}
