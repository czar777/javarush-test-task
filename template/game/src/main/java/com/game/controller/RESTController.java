package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/rest")
public class RESTController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public List<Player> getPlayers(@RequestParam(name = "name", required = false) String name,
                                   @RequestParam(name = "title", required = false) String title,
                                   @RequestParam(name = "race", required = false) Race race,
                                   @RequestParam(name = "profession", required = false) Profession profession,
                                   @RequestParam(name = "after", required = false) Long after,
                                   @RequestParam(name = "before", required = false) Long before,
                                   @RequestParam(name = "banned", required = false) Boolean banned,
                                   @RequestParam(name = "minExperience", required = false) Integer minExperience,
                                   @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
                                   @RequestParam(name = "minLevel", required = false) Integer minLevel,
                                   @RequestParam(name = "maxLevel", required = false) Integer maxLevel,
                                   @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(name = "pageSize", required = false) Integer pageSize,
                                   @RequestParam(name = "order", required = false) PlayerOrder order) {

        if (order == null) {
            order = PlayerOrder.ID;
        }

        if (pageNumber == null) {
            pageNumber = 0;
        }

        if (pageSize == null) {
            pageSize = 3;
        }

        List<Player> playersFiltred = playerService.getAllPlayers();

        if (name != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getName().contains(name))
                    .collect(Collectors.toList());
        }

        if (title != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getTitle().contains(title))
                    .collect(Collectors.toList());
        }

        if (race != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getRace() == race)
                    .collect(Collectors.toList());
        }

        if (profession != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getProfession() == profession)
                    .collect(Collectors.toList());
        }

        if (after != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getBirthday().getTime() > after)
                    .collect(Collectors.toList());
        }

        if (before != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getBirthday().getTime() < before)
                    .collect(Collectors.toList());
        }

        if (banned != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getBanned() == banned)
                    .collect(Collectors.toList());
        }

        if (minExperience != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getExperience() > minExperience)
                    .collect(Collectors.toList());
        }

        if (maxExperience != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getExperience() < maxExperience)
                    .collect(Collectors.toList());
        }

        if (minLevel != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getLevel() > minLevel)
                    .collect(Collectors.toList());
        }

        if (maxLevel != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getLevel() < maxLevel)
                    .collect(Collectors.toList());
        }

        PlayerOrder finalOrder = order;

        playersFiltred = playersFiltred.stream()
                .sorted((o1, o2) -> {
                    if (finalOrder == (PlayerOrder.ID)) {
                        return (int) (o1.getId() - o2.getId());
                    } else if (finalOrder.equals(PlayerOrder.BIRTHDAY)) {
                        return (o1.getBirthday().compareTo(o2.getBirthday()));
                    } else if (finalOrder.equals(PlayerOrder.EXPERIENCE)) {
                        return o1.getExperience() - o2.getExperience();
                    } else if (finalOrder.equals(PlayerOrder.LEVEL)) {
                        return (o1.getLevel() - o2.getLevel());
                    } else {
                        return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
                    }
                })
                .collect(Collectors.toList());
//        AtomicInteger counter = new AtomicInteger();
//        Integer finalPageSize = pageSize;
//        final Collection<List<Player>> partitionedList =
//                playersFiltred.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / finalPageSize))
//                        .values();
//        return new ArrayList<>(partitionedList).get(pageNumber);
        List<List<Player>> partitionedList = new ArrayList<>();

        List<Player> list = new ArrayList<>();
        for (int i = 0; i < playersFiltred.size(); i++) {
            list.add(playersFiltred.get(i));
            if ((i + 1) % pageSize == 0 || (i + 1) == playersFiltred.size()) {
                partitionedList.add(list);
                list = new ArrayList<>();
            }

        }
        if (partitionedList.size() != 0) {
            return partitionedList.get(pageNumber);
        } else {
            return playersFiltred;
        }
    }

    @GetMapping("/players/count")
    public int getPlayersCount(@RequestParam(name = "name", required = false) String name,
                               @RequestParam(name = "title", required = false) String title,
                               @RequestParam(name = "race", required = false) Race race,
                               @RequestParam(name = "profession", required = false) Profession profession,
                               @RequestParam(name = "after", required = false) Long after,
                               @RequestParam(name = "before", required = false) Long before,
                               @RequestParam(name = "banned", required = false) Boolean banned,
                               @RequestParam(name = "minExperience", required = false) Integer minExperience,
                               @RequestParam(name = "maxExperience", required = false) Integer maxExperience,
                               @RequestParam(name = "minLevel", required = false) Integer minLevel,
                               @RequestParam(name = "maxLevel", required = false) Integer maxLevel) {
        List<Player> playersFiltred = playerService.getAllPlayers();

        if (name != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getName().contains(name))
                    .collect(Collectors.toList());
        }

        if (title != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getTitle().contains(title))
                    .collect(Collectors.toList());
        }

        if (race != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getRace() == race)
                    .collect(Collectors.toList());
        }

        if (profession != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getProfession() == profession)
                    .collect(Collectors.toList());
        }

        if (after != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getBirthday().getTime() > after)
                    .collect(Collectors.toList());
        }

        if (before != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getBirthday().getTime() < before)
                    .collect(Collectors.toList());
        }

        if (banned != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getBanned() == banned)
                    .collect(Collectors.toList());
        }

        if (minExperience != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getExperience() > minExperience)
                    .collect(Collectors.toList());
        }

        if (maxExperience != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getExperience() < maxExperience)
                    .collect(Collectors.toList());
        }

        if (minLevel != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getLevel() > minLevel)
                    .collect(Collectors.toList());
        }

        if (maxLevel != null) {
            playersFiltred = playersFiltred.stream()
                    .filter(x -> x.getLevel() < maxLevel)
                    .collect(Collectors.toList());
        }

        return playersFiltred.size();
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable Object id) {

        Long longId;
        try {
            longId = Long.parseLong(id.toString());
            if (longId == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Player player = playerService.getPlayer(longId);


        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return player;
    }

    @PostMapping("/players")
    public Player addNewPlayer(@RequestBody Player player) {
        if (player.getBanned() == null) {
            player.setBanned(false);
        }

        playerService.savePlayer(player);
        return player;
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@RequestBody Player newPlayer, @PathVariable Object id) {
        System.out.println("start update player");
        Player oldPlayer = getPlayer(id);
        System.out.println("got player: " + oldPlayer.toString());
        System.out.println("new player: " + newPlayer.toString());

        if (newPlayer.getTitle() != null) {
            oldPlayer.setTitle(newPlayer.getTitle());
        }

        if (newPlayer.getName() != null) {
            oldPlayer.setName(newPlayer.getName());
        }

        if (newPlayer.getRace() != null) {
            oldPlayer.setRace(newPlayer.getRace());
        }

        if (newPlayer.getProfession() != null) {
            oldPlayer.setProfession(newPlayer.getProfession());
        }

        if (newPlayer.getExperience() != null) {
            oldPlayer.setExperience(newPlayer.getExperience());
        }

        if (newPlayer.getBanned() != null) {
            oldPlayer.setBanned(newPlayer.getBanned());
        }

        if (newPlayer.getBirthday() != null && newPlayer.getBirthday().getTime() != oldPlayer.getBirthday().getTime()) {
            oldPlayer.setBirthday(newPlayer.getBirthday());
        }

        playerService.savePlayer(oldPlayer);
        return oldPlayer;
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Object id) {
        Long longId;
        try {
            longId = Long.parseLong(id.toString());
            if (longId == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Player player = playerService.getPlayer(longId);

        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        playerService.deletePlayer(longId);
    }
}
