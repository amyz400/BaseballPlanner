package com.baseballPlanner.service;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.FieldPositionsConfiguration;
import com.baseballPlanner.models.GameModel;
import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.PlayerDao;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by amy on 5/11/17.
 */
@Service("gameService")
public class GameService {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepo playerRepo;

    private Map<PlayerDao, FieldPositionEnum> prevInningPositionsMap = new DualHashBidiMap<PlayerDao, FieldPositionEnum>() {
    };

    public GameModel createGame(LocalDate datePlayed) {
        GameModel gameModel = new GameModel();
        gameModel.setDatePlayed(datePlayed);
        List<InningDao> prevModel = null;
        for (int i=0; i<6; i++) {

            gameModel.getInningMap().put(i+1, createInning(prevModel));
        }

        // save game Model
        return gameModel;
    }

    private Map<PlayerDao, FieldPositionEnum> createInning(List<InningDao> prevInning) {


        DualHashBidiMap<PlayerDao, FieldPositionEnum> currentInningMap =  new DualHashBidiMap<>();

        // get all possible FieldPositions to make sure they are all used up
        List<FieldPositionEnum> infieldPositions = new ArrayList<>();
        infieldPositions.addAll(FieldPositionsConfiguration.infieldPositions);
        List<FieldPositionEnum> outfieldPositions = new ArrayList<>();
        outfieldPositions.addAll(FieldPositionsConfiguration.outfieldPositions);
        List<FieldPositionEnum> premiumPositions = new ArrayList<>();
        premiumPositions.addAll(FieldPositionsConfiguration.premiumPositions);

        // get players
        List<PlayerDao> players = (List<PlayerDao>)playerRepo.findAll();
        // shuffle collection to get a random order
        Collections.shuffle(players);

        for(PlayerDao player : players) {

            // what position did the player have last inning
            FieldPositionEnum prevPosition = prevInningPositionsMap.get(player.getId());

            // if not outfield, then try and pick outfield first
            if ((null == prevPosition || -1 == FieldPositionsConfiguration.outfieldPositions.indexOf(prevPosition)) &&
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
        prevInningPositionsMap.putAll(currentInningMap);
        return currentInningMap;
        //return sortCurrentInning(currentInningMap);
    }

    private  Map<PlayerDao, FieldPositionEnum> sortCurrentInning(Map<PlayerDao, FieldPositionEnum> inning) {
        Map<PlayerDao, FieldPositionEnum> sortedMap = new LinkedHashMap<>();

        //sort by key, a,b,c..., and put it into the "result" map
        inning.entrySet().stream()
                .sorted(Map.Entry.<PlayerDao, FieldPositionEnum>comparingByKey())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;

    }
}
