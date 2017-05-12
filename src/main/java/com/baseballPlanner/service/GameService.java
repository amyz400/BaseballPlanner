package com.baseballPlanner.service;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.FieldPositionsConfiguration;
import com.baseballPlanner.models.GameModel;
import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by amy on 5/11/17.
 */
@Service("gameService")
public class GameService {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepo playerRepo;

    public GameModel createGame(LocalDate datePlayed) {
        GameModel gameModel = null;
        List<InningDao> prevModel = null;
        for (int i=0; i<6; i++) {
            List<InningDao> currentInning = createInning(prevModel);
            gameModel.getInningList().add(currentInning);
            prevModel = currentInning
        }
        return gameModel;
    }

    private List<InningDao> createInning(List<InningDao> prevInning) {
        // get all possible FieldPositions to make sure they are all used up
        Set<FieldPositionEnum> fieldPositionsSet = new HashSet<>();
        fieldPositionsSet.addAll(FieldPositionsConfiguration.infieldPositions);
        fieldPositionsSet.addAll(FieldPositionsConfiguration.outfieldPositions);
        fieldPositionsSet.addAll(FieldPositionsConfiguration.premiumPositions);
        fieldPositionsSet.addAll(FieldPositionsConfiguration.miscPositions);

        List<InningDao> inningList = new ArrayList<>();

        // get players
        Iterable<PlayerDao> players = playerRepo.findAll();
        List<PlayerDao> playerList = new ArrayList<>();
        players.forEach(playerList::add);
        // shuffle collection to get a random order
        Collections.shuffle(playerList);

        for(PlayerDao player : playerList) {

            // what position did the player have last inning

            // if outfield, then not infield, premium and then misc

            // if not outfield, then try and pick outfield first

            // create and save InningDao
        }

        return inningList;
    }
}
