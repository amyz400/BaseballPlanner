package com.baseballPlanner.service;

import com.baseballPlanner.service.repositories.PlayerRepo;
import com.baseballPlanner.tx.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aziring on 6/3/17.
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepo playerRepo;

    public List<PlayerDao> getPlayers() {
            Iterable<PlayerDao> source = playerRepo.findAll();
            List<PlayerDao> target = new ArrayList<>();
            source.forEach(target::add);

            return target;
    }

    public boolean addPlayer(PlayerDao playerDao) {

        boolean success = true;
        try {
            playerRepo.save(playerDao);
        } catch (Exception e) {
            success = false;
        }

        return success;
    }
}
