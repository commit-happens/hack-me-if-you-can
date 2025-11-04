package com.hackme.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Player Entity Class - Represents a database table for storing player information.
 * 
 * ENTITY CONCEPTS FOR BEGINNERS:
 * 
 * 1. WHAT IS AN ENTITY?
 *    - An entity is a Java class that represents a database table
 *    - Each instance of this class represents a row in the table
 *    - The class fields represent table columns
 * 
 * 2. JPA (Java Persistence API):
 *    - JPA is a specification for managing relational data in Java
 *    - It allows you to work with databases using Java objects instead of SQL
 *    - Hibernate is the implementation of JPA that Spring Boot uses
 * 
 * 3. ANNOTATIONS EXPLAINED:
 *    - @Entity: Marks this class as a database entity
 *    - @Table: Specifies the database table name (optional, defaults to class name)
 *    - @Id: Marks the primary key field
 *    - @GeneratedValue: Tells database to auto-generate the ID value
 *    - @Column: Configures database column properties
 * 
 * 4. VALIDATION ANNOTATIONS:
 *    - @NotBlank: Field cannot be null, empty, or just whitespace
 *    - @NotNull: Field cannot be null
 *    - @Size: Validates the length of strings
 * 
 * DATABASE TABLE STRUCTURE:
 * +----+-----------+-------+
 * | ID | NICKNAME  | SCORE |
 * +----+-----------+-------+
 * | 1  | player123 | 85    |
 * | 2  | gamer456  | 92    |
 * +----+-----------+-------+
 * 
 * @author Hack Me If You Can Team
 * @version 1.0
 * @since Java 21
 */
@Entity // Tells JPA this is a database entity (table)
@Table(name = "players") // Specifies the table name in database
public class Player {
    
    /**
     * Primary Key - Unique identifier for each player.
     * 
     * @Id marks this as the primary key
     * @GeneratedValue tells database to auto-increment this value
     * IDENTITY strategy means database will handle ID generation
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Player's nickname - must be unique across all players.
     * 
     * @Column configures the database column:
     * - unique = true: No two players can have same nickname
     * - nullable = false: This field is required
     * 
     * Validation annotations ensure data quality:
     * - @NotBlank: Cannot be null, empty, or just spaces
     * - @Size: Must be between 3 and 50 characters
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Nickname cannot be blank")
    @Size(min = 3, max = 50, message = "Nickname must be between 3 and 50 characters")
    private String nickname;
    
    /**
     * Player's game score - defaults to 0 for new players.
     * 
     * @NotNull ensures score is always set (but can be 0)
     * Default value of 0 is set when creating new Player objects
     */
    @Column(nullable = false)
    @NotNull(message = "Score cannot be null")
    private Integer score = 0;
    
    /**
     * CONSTRUCTORS - Different ways to create Player objects
     * 
     * Java requires constructors to create objects. You can have multiple constructors
     * with different parameters (this is called "constructor overloading").
     */
    
    /**
     * Default constructor - creates Player with default values.
     * 
     * JPA requires a no-argument constructor to create objects when reading from database.
     * When you create: new Player(), you get:
     * - id: null (will be set by database)
     * - nickname: null 
     * - score: 0 (default value we set above)
     */
    public Player() {
        // Empty constructor - JPA will set values from database
    }
    
    /**
     * Constructor with parameters - creates Player with specific values.
     * 
     * This is convenient when you know the nickname and score upfront.
     * Example: new Player("gamer123", 75)
     * 
     * @param nickname The player's unique nickname
     * @param score    The player's initial score
     */
    public Player(String nickname, Integer score) {
        this.nickname = nickname;
        this.score = score;
        // id will be null and set by database when saved
    }
    
    /**
     * GETTER AND SETTER METHODS
     * 
     * In Java, fields are usually private for security (encapsulation).
     * We provide public getter/setter methods to access and modify them.
     * 
     * WHY USE GETTERS/SETTERS?
     * - Control access to data
     * - Add validation when setting values
     * - Allow frameworks (like Spring/JPA) to work with your objects
     * - Follow Java naming conventions
     */
    
    /**
     * Gets the player's unique ID.
     * @return the player's database ID (null if not saved yet)
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the player's ID (usually done by JPA/database).
     * @param id the unique identifier
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the player's nickname.
     * @return the player's unique nickname
     */
    public String getNickname() {
        return nickname;
    }
    
    /**
     * Sets the player's nickname.
     * @param nickname the unique nickname (must pass validation)
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * Gets the player's current score.
     * @return the player's game score
     */
    public Integer getScore() {
        return score;
    }
    
    /**
     * Sets the player's score.
     * @param score the new score (cannot be null)
     */
    public void setScore(Integer score) {
        this.score = score;
    }
    
    /**
     * String representation of Player object.
     * 
     * The @Override annotation indicates we're replacing the default toString() method
     * from the Object class. This is useful for debugging and logging.
     * 
     * Example output: "Player{id=1, nickname='gamer123', score=85}"
     * 
     * @return formatted string representation of the player
     */
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", score=" + score +
                '}';
    }
}