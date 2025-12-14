/**
 * Player je třída reprezentující hráče v databázi (@Entity označuje, že tato třída je databázová tabulka).
 * Každá instance (objekt) této třídy = jeden řádek v tabulce
 */

package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "players")
public class Player {

    @Id // Primární klíč, generován automaticky databází
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long playerId;
    
    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "score", nullable = false)
    private Integer score;
}
