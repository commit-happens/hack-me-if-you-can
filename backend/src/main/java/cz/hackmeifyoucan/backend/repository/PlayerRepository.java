/* This file defines a repository interface — 
the bridge between the Java code and the database */

package cz.hackmeifyoucan.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cz.hackmeifyoucan.backend.entity.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

}
