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

    private Map<PlayerDao, FieldPositionEnum> prevInningPositionsMap = new HashMap<>();

    public GameModel createGame(LocalDate datePlayed) {
        GameModel gameModel = null;
        List<InningDao> prevModel = null;
        for (int i=0; i<6; i++) {
            List<InningDao> currentInning = createInning(prevModel);
            gameModel.getInningList().add(currentInning);
        }
        return gameModel;
    }

    private List<InningDao> createInning(List<InningDao> prevInning) {

        Map<PlayerDao, FieldPositionEnum> currentInningMap = new HashMap<>();

        // get all possible FieldPositions to make sure they are all used up
        List<FieldPositionEnum> infieldPositions = new ArrayList<>();
        infieldPositions.addAll(FieldPositionsConfiguration.infieldPositions);
        List<FieldPositionEnum> outfieldPositions = new ArrayList<>();
        outfieldPositions.addAll(FieldPositionsConfiguration.outfieldPositions);
        List<FieldPositionEnum> premiumPositions = new ArrayList<>();
        premiumPositions.addAll(FieldPositionsConfiguration.premiumPositions);

        List<InningDao> inningList = new ArrayList<>();

        // get players
        Iterable<PlayerDao> players = playerRepo.findAll();
        List<PlayerDao> playerList = new ArrayList<>();
        players.forEach(playerList::add);
        // shuffle collection to get a random order
        Collections.shuffle(playerList);

        for(PlayerDao player : playerList) {

            // what position did the player have last inning
            FieldPositionEnum prevPosition = prevInningPositionsMap.get(player.getId());

            // if not outfield, then try and pick outfield first
            if (-1 == FieldPositionsConfiguration.outfieldPositions.indexOf(prevPosition) &&
                    !outfieldPositions.isEmpty()){
                currentInningMap.put(player, outfieldPositions.remove(0));
            } else if (!premiumPositions.isEmpty()) {
                currentInningMap.put(player, premiumPositions.remove(0));
            } else if (!infieldPositions.isEmpty()) {
                currentInningMap.put(player, infieldPositions.remove(0));
            } else {
                // if no positions are left then the player is on the bench
                currentInningMap.put(player, FieldPositionEnum.BENCH);
            }

            // create and save InningDao
        }
        prevInningPositionsMap.clear();
        currentInningMap.putAll(currentInningMap);

        return inningList;
    }
}
