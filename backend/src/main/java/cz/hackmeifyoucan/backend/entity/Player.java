/**
 * Player je třída reprezentující hráče v databázi (@Entity označuje, že tato třída je databázová tabulka).
 * Každá instance (objekt) této třídy = jeden řádek v tabulce
 */

package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;
}
