package com.game.repository;

import com.game.entity.Player;

import java.util.List;

public interface PlayerDAO {
    public void savePlayer(Player player);

    public List<Player> getAllPlayers();

    public Player getPlayer(long id);

    public void deletePlayer(long id);
}
