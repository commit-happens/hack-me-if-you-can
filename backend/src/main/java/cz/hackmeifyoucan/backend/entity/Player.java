package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO	)
	private Long playerId;
	private String nickname;
	private Integer score;

}