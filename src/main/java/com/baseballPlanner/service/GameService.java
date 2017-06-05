package com.baseballPlanner.service;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.FieldPositionsConfiguration;
import com.baseballPlanner.models.GameModel;
import com.baseballPlanner.models.PlayerState;
import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.PlayerDao;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

    private GameModel gameModel;

    private Map<Integer, PlayerState> playerStateMap;

    public GameModel createGame(LocalDate datePlayed) {

        playerStateMap = new HashMap<>();
        gameModel = new GameModel();
        gameModel.setDatePlayed(datePlayed);

        for (int i=0; i<6; i++) {
            gameModel.getInningMap().put(i+1, createInning());
        }

        // save game Model
        return gameModel;
    }

    private List<FieldPositionEnum> populatePositions(List<FieldPositionEnum> positions) {
        List<FieldPositionEnum> positionList = new ArrayList<>();
        positionList.addAll(positions);
        Collections.shuffle(positions);

        return positionList;
    }

    private Map<PlayerDao, FieldPositionEnum> createInning() {

        Map<PlayerDao, FieldPositionEnum> currentInningMap =  new HashMap<>();

        // get all possible FieldPositions to make sure they are all used up
        List<FieldPositionEnum> infieldPositions = populatePositions(FieldPositionsConfiguration.infieldPositions);
        List<FieldPositionEnum> outfieldPositions = populatePositions(FieldPositionsConfiguration.outfieldPositions);
        List<FieldPositionEnum> premiumPositions = populatePositions(FieldPositionsConfiguration.premiumPositions);

        // shuffle players to get a random order but put players with their last position on the BENCH at the front
        // to avoid them getting the bench twice
        List<PlayerDao> players = shufflePlayers(playerStateMap);

        for(PlayerDao player : players) {

            // find playerState for this game
            PlayerState playerState = playerStateMap.get(player.getId());
            if (null == playerState) {
                playerState = initializePlayerState(player);
                playerStateMap.put(player.getId(), playerState);
            }

            FieldPositionEnum currentPosition = pickPosition(playerState, outfieldPositions, infieldPositions, premiumPositions);

            currentInningMap.put(player, currentPosition);
            playerState.getInningPositions().push(currentPosition);
        }
        inningCleanup(currentInningMap, playerStateMap, outfieldPositions, infieldPositions, premiumPositions);
        currentInningMap = sortCurrentInning(currentInningMap);
        return currentInningMap;
    }

    private List<PlayerDao> shufflePlayers(Map<Integer, PlayerState> playerStateMap) {
        List<PlayerDao> finalPlayers = new ArrayList<>();
        List<PlayerDao> tempPlayers = (List<PlayerDao>)playerRepo.findAll();
        Collections.shuffle(tempPlayers);

        List<PlayerState> prevInningBenched = findPrevInningBenchedPlayers(playerStateMap);
        // make this a lambda
        for (PlayerState ps : prevInningBenched) {
            finalPlayers.add(ps.getPlayer());
            tempPlayers.remove(ps.getPlayer());
        }
        // any player list in the temp list should be added to the final list
        finalPlayers.addAll(tempPlayers);

       return finalPlayers;
    }

    private List<PlayerState> findPrevInningBenchedPlayers(Map<Integer, PlayerState> playerStateMap) {
        List<PlayerState> benched = new ArrayList<>();
        for(Map.Entry<Integer, PlayerState> entry : playerStateMap.entrySet()) {
            if (entry.getValue().getInningPositions().getFirst() == FieldPositionEnum.BENCH) {
                benched.add(entry.getValue());
            }
        }
        return benched;
    }

    private PlayerState initializePlayerState(PlayerDao player) {
        PlayerState ps = new PlayerState();
        ps.setPlayer(player);
        ps.setHowManyTimesInPremiumPositions(0);
        ps.setInningPositions(new LinkedList<FieldPositionEnum>());

        return ps;
    }

    private FieldPositionEnum pickPosition(PlayerState playerState, List<FieldPositionEnum> outfieldPositions,
                                           List<FieldPositionEnum> infieldPositions, List<FieldPositionEnum> premiumPositions) {
        FieldPositionEnum pos = FieldPositionEnum.NONE;

        LinkedList<FieldPositionEnum> previousPositions = new LinkedList<>();
        previousPositions.addAll(playerState.getInningPositions());
        FieldPositionEnum prevPosition = FieldPositionEnum.NONE;
        if (previousPositions.size() > 0) { prevPosition = previousPositions.get(0); }

        // if not outfield, then try and pick outfield first
        if (((FieldPositionEnum.NONE == prevPosition && !outfieldPositions.isEmpty()) ||
                (-1 == FieldPositionsConfiguration.outfieldPositions.indexOf(prevPosition)) &&
                        !outfieldPositions.isEmpty() &&
                        -1 == previousPositions.lastIndexOf(outfieldPositions.get(0)))){
            pos = outfieldPositions.remove(0);

        } // if there are premium positions available and this player hasn't exceeded his configured number of times in a premium position
        // and the player's last position wasn't in this grouping, then give a premium position
        else if (!premiumPositions.isEmpty() &&
                -1 == FieldPositionsConfiguration.premiumPositions.indexOf(prevPosition) &&
                -1 == previousPositions.lastIndexOf(premiumPositions.get(0)) &&
                playerState.getHowManyTimesInPremiumPositions() < playerState.getPlayer().getTimesInPremium()) {
            pos = premiumPositions.remove(0);
            playerState.addToHowManyTimesInPremiumPositions(1);

        } // if there are infield positions left and the player's last position wasn't in the infield, then give an infield position
        else if (!infieldPositions.isEmpty() &&
                -1 == FieldPositionsConfiguration.infieldPositions.indexOf(prevPosition) &&
                -1 == previousPositions.lastIndexOf(infieldPositions.get(0))) {
            pos = infieldPositions.remove(0);

        } else {
            // if no positions are left then the player is on the bench
            // DON'T WORRY, we'll loosen the rules to clean up the inning and ensure all positions have a player
            pos = FieldPositionEnum.BENCH;
            playerState.addToHowManyTimesOnBench(1);
        }
        return pos;
    }

    private void inningCleanup(Map<PlayerDao, FieldPositionEnum> currentInningMap, Map<Integer, PlayerState> playerStateMap,
                               List<FieldPositionEnum> outfieldPositions, List<FieldPositionEnum> infieldPositions,
                               List<FieldPositionEnum> premiumPositions) {

        // do any of the position lists still have entries?
        // if yes, find benched players and see how many times they have benched
        List<PlayerState> benchedPlayers = findPrevInningBenchedPlayers(playerStateMap);

        // user a comparator to order this list
        //sort by key, a,b,c..., and put it into the "result" map
        List<PlayerState> sortedList = new ArrayList<>();
        benchedPlayers.stream()
                .sorted()
                .forEachOrdered(x -> sortedList.add(x));

        for (Iterator<PlayerState> iterator = sortedList.iterator(); iterator.hasNext();) {
            PlayerState ps = iterator.next();
            if (!premiumPositions.isEmpty()) {
                currentInningMap.put(ps.getPlayer(), premiumPositions.get(0));
                iterator.remove();
                premiumPositions.remove(0);
                ps.addToHowManyTimesInPremiumPositions(1);
                ps.deleteFromHowManyTimesOnBench(1);
            } else if (!infieldPositions.isEmpty()) {
                currentInningMap.put(ps.getPlayer(), infieldPositions.get(0));
                iterator.remove();
                infieldPositions.remove(0);
                ps.deleteFromHowManyTimesOnBench(1);

            } else if (!outfieldPositions.isEmpty()) {
                currentInningMap.put(ps.getPlayer(), outfieldPositions.get(0));
                iterator.remove();
                outfieldPositions.remove(0);
                ps.deleteFromHowManyTimesOnBench(1);
            }
        }
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
