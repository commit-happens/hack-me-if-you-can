/* Rozhraní repository — most mezi Java kódem a databází */

package cz.hackmeifyoucan.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cz.hackmeifyoucan.backend.entity.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

}