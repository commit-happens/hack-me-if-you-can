/**
 * Player je třída reprezentující hráče v databázi (@Entity označuje, že tato třída je databázová tabulka).
 * Každá instance (objekt) této třídy = jeden řádek v tabulce
 */

package cz.hackmeifyoucan.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Player {

    @Id // Primární klíč, generován automaticky databází
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;
    
    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "score", nullable = false)
    private Integer score;

    // Konstruktor bez parametrů - potřebuje ho JPA (databázový framework)
    public Player() {}

    // Konstruktor se všemi parametry - používáme pro vytvoření nového hráče
    public Player(Long playerId, String nickname, Integer score) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.score = score;
    }

    // Gettery - metody pro čtení hodnot proměnných
    public Long getPlayerId() {
        return playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getScore() {
        return score;
    }

    // Settery - metody pro změnu hodnot proměnných
    protected void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
