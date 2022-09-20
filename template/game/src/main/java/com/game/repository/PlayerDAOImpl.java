package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Override
    public void savePlayer(Player player) {
        System.out.println("start saveOrUpdate");
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(player);
    }

    @Override
    public List<Player> getAllPlayers() {
        Session session = entityManager.unwrap(Session.class);
        List<Player> allPlayers = session.createQuery("from Player", Player.class).getResultList();
        return allPlayers;
    }

    @Override
    public Player getPlayer(long id) {
        Session session = entityManager.unwrap(Session.class);
        Player player = session.get(Player.class, id);
        return player;
    }

    @Override
    public void deletePlayer(long id) {
        Session session = entityManager.unwrap(Session.class);
        Query<Player> query = session.createQuery("delete from Player where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

}
