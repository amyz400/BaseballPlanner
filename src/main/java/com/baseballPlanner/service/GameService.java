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

    /**
     * Create a game
     * @param datePlayed Date that the game is being played
     * @return a Model of the game
     */
    public GameModel createGame(LocalDate datePlayed) {

        return createGame(datePlayed, new ArrayList<>());
    }

    /**
     * Create a game
     * @param datePlayed Date that the game is being played
     * @param playerIds List of player ids of players that are playing in the game; list is empty if all players are playing
     * @return a Model of the game
     */
    public GameModel createGame(LocalDate datePlayed, List<Integer> playerIds) {

        playerStateMap = new HashMap<>();
        gameModel = new GameModel();
        gameModel.setDatePlayed(datePlayed);

        for (int i=0; i<6; i++) {
            gameModel.getInningMap().put(i+1, createInning(playerIds));
        }

        // save game Model
        return gameModel;

    }

    /**
     * Populate and shuffle a list of positions
     * @param positions static list to use to populate
     * @return shuffled positions
     */
    public List<FieldPositionEnum> populatePositions(List<FieldPositionEnum> positions) {
        List<FieldPositionEnum> positionList = new ArrayList<>();
        positionList.addAll(positions);
        Collections.shuffle(positions);

        return positionList;
    }

    /**
     * Create an innning for the game.  Pick positions for all the players and then sort the inning using the
     * same comparator as all the other innings.
     * @param playersInGame List of players that are playing this game
     * @return Map of players and their selected positions for the inning
     */
    private Map<PlayerDao, FieldPositionEnum> createInning(List<Integer> playersInGame) {

        Map<PlayerDao, FieldPositionEnum> currentInningMap =  new HashMap<>();

        // get all possible FieldPositions to make sure they are all used up
        List<FieldPositionEnum> infieldPositions = populatePositions(FieldPositionsConfiguration.infieldPositions);
        List<FieldPositionEnum> outfieldPositions = populatePositions(FieldPositionsConfiguration.outfieldPositions);
        List<FieldPositionEnum> premiumPositions = populatePositions(FieldPositionsConfiguration.premiumPositions);

        // shuffle players to get a random order but put players with their last position on the BENCH at the front
        // to avoid them getting the bench twice
        List<PlayerDao> players = shufflePlayers(playerStateMap, playersInGame);

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

    /**
     * Return a shuffled list of all the players playing this game.  If a player was benched last inning, make sure
     * they are at the top of the list to get a position to reduce chance that they'll be benched again.
     * @param playerStateMap mapping of all the players and information about previous game play
     * @param playersInGame Subset of players in the game. If this list is empty, use all the available players
     * @return Shuffle dlist of players
     */
    public List<PlayerDao> shufflePlayers(Map<Integer, PlayerState> playerStateMap, List<Integer> playersInGame) {

        List<PlayerDao> finalPlayers = new ArrayList<>();
        List<PlayerDao> tempPlayers = null;
        if (playersInGame.isEmpty()) {
            tempPlayers = (List<PlayerDao>)playerRepo.findAll();
        } else {
            tempPlayers = (List<PlayerDao>)playerRepo.findAll(playersInGame);
        }
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

    /**
     * Find list of players who were benched last inning
     * @param playerStateMap mapping of all the players and information about previous game play
     * @return list of players who were benched
     */
    public List<PlayerState> findPrevInningBenchedPlayers(Map<Integer, PlayerState> playerStateMap) {
        List<PlayerState> benched = new ArrayList<>();
        for(Map.Entry<Integer, PlayerState> entry : playerStateMap.entrySet()) {
            if (entry.getValue().getInningPositions().getFirst() == FieldPositionEnum.BENCH) {
                benched.add(entry.getValue());
            }
        }
        return benched;
    }

    /**
     * Initialize a player state for the start of a game.
     * @param player Player that this state is being created for
     * @return initialized player state
     */
    public PlayerState initializePlayerState(PlayerDao player) {
        PlayerState ps = new PlayerState();
        ps.setPlayer(player);
        ps.setHowManyTimesInPremiumPositions(0);
        ps.setInningPositions(new LinkedList<FieldPositionEnum>());

        return ps;
    }

    /**
     * Pick a position based on the following rules for a player:
     * <ol>
     * <li>If there are outfield positions, the player's last inning's position wasn't in the outfield, and the player has never
     * played in the next available outfield position, then pick the next outfield position.</li>
     * <li>If there are premium positions available, this player hasn't exceeded his times in a premium position, the player's
     * last innning's position wasn't in a premium position, and the player hasn't played the next premium poistion, then pick
     * the next premium position.</li>
     * <li>If there are infield positions, the player's last inning's position wasn't in the infield, and the player has never
     * played in the next available infield position, then pick the next infield position.</li>
     * <li>If the player didn't meet any of the other rules, then put him in on the bench.</li>
     * </ol>
     * @param playerState The state of the game for the player we are picking a position for.
     * @param outfieldPositions List of all the available outfield positions for this inning.
     * @param infieldPositions List of all the available infield positions for this inning.
     * @param premiumPositions List of all the available premium positions for this inning.
     * @return Position selected for this player
     */
    public FieldPositionEnum pickPosition(PlayerState playerState, List<FieldPositionEnum> outfieldPositions,
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

    /**
     * pickPosition does not work perfectly, especially in the later innings of the game.  Not all available positions
     * will be assigned, and yet, some players will be benched.  This method will look at all the benched players and
     * assign them any remaining positions.  Players that have been benched the most times are given positions first.
     * The premium positions are assigned first, then the infield positions and finally, the outfield positions.  It is
     * assumed that order is the most important in where players should be playing.
     * @param currentInningMap Which players are playing which positions for the current inning
     * @param playerStateMap mapping of all the players and information about previous game play
     * @param outfieldPositions List of any available outfield positions
     * @param infieldPositions List of any available infield positions
     * @param premiumPositions List of any available premium positions
     */
    public void inningCleanup(Map<PlayerDao, FieldPositionEnum> currentInningMap, Map<Integer, PlayerState> playerStateMap,
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

    /**
     * Sort the current inning map by player name.
     * @param inning map of the current inning with position assignments to player
     * @return sorted map
     */
    public  Map<PlayerDao, FieldPositionEnum> sortCurrentInning(Map<PlayerDao, FieldPositionEnum> inning) {
        Map<PlayerDao, FieldPositionEnum> sortedMap = new LinkedHashMap<>();

        //sort by key, a,b,c..., and put it into the "result" map
        inning.entrySet().stream()
                .sorted(Map.Entry.<PlayerDao, FieldPositionEnum>comparingByKey())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;

    }
}
