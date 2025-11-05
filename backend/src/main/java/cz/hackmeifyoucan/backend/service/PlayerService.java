/* This file defines a service interface, which describes the business logic layer
between the controllers (API) and repositories (database)*/

package cz.hackmeifyoucan.backend.service;

import java.util.List;

import cz.hackmeifyoucan.backend.entity.Player;

public interface PlayerService {

    List<Player> getPlayers();
    Player getPlayerById(Long playerId);
    Player addPlayer(Player player);
    Player updatePlayer(Long playerId, Player player);
    Player deletePlayer(Long playerId);

}
