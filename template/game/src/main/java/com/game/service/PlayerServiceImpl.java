package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerDAO playerDAO;

    @Override
    @Transactional
    public void savePlayer(Player player) {
        System.out.println("start Service");
        if (player.getName() == null || player.getName().length() > 12) {
            System.out.println("setName");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        if (player.getTitle() == null || player.getTitle().length() > 30) {
            System.out.println("setTitle");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        if (player.getExperience() < 0 || player.getExperience() > 10_000_000) {
            System.out.println("setExperience");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        player.setLevel((int) (Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100);
        player.setUntilNextLevel( 50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());


        Long longBirthday = player.getBirthday().getTime();
        Long date2000 = new Date(100, 0, 1).getTime();
        Long date3000 = new Date(1100, 0, 1).getTime();
        if (longBirthday < 0 || date2000 > longBirthday || date3000 < longBirthday ) {
            System.out.println("setBirthday");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        System.out.println("player created: " + player);
        playerDAO.savePlayer(player);
    }

    @Override
    @Transactional
    public List<Player> getAllPlayers() {
        return playerDAO.getAllPlayers();
    }

    @Override
    @Transactional
    public Player getPlayer(long id) {
        Player player = playerDAO.getPlayer(id);
        return player;
    }

    @Override
    @Transactional
    public void deletePlayer(long id) {
        playerDAO.deletePlayer(id);
    }

}
