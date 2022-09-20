package com.game.service;

import com.game.entity.Player;

import java.util.List;

public interface PlayerService {
    public void savePlayer(Player player);

    public List<Player> getAllPlayers();

    public Player getPlayer(long id);

    public void deletePlayer(long id);

}
