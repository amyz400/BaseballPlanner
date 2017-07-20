package com.baseballPlanner.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.FieldPositionsConfiguration;
import com.baseballPlanner.models.PlayerState;
import com.baseballPlanner.service.repositories.PlayerRepo;
import com.baseballPlanner.tx.dao.PlayerDao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by aziring on 6/5/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Test
    public void populatePositionsTest() {
        List<FieldPositionEnum> infieldPositions = gameService.populatePositions(FieldPositionsConfiguration.infieldPositions);

        for (FieldPositionEnum pos : infieldPositions) {
            assertTrue(pos == FieldPositionEnum.FIRSTB ||
                pos == FieldPositionEnum.SECONDB ||
                pos == FieldPositionEnum.CATCHER ||
                pos == FieldPositionEnum.SS ||
                pos == FieldPositionEnum.THIRDB);
        }
    }

    @Test
    public void findPrevInningBenchedPlayersTest()  {
        List<Integer> expectedBenchedPlayers = new ArrayList<>();
        Map<Integer, PlayerState> playerStateMap = new HashMap<>();

        List<PlayerDao> players = createPlayers();
        for(int i=0; i<players.size(); i++) {

            PlayerState ps = new PlayerState();

            ps.setHowManyTimesInPremiumPositions(1);
            ps.setPlayer(players.get(i));

            if (i == 1) { expectedBenchedPlayers.add(players.get(i).getId()); }
            if (i == 3) { expectedBenchedPlayers.add(players.get(i).getId()); }
            if (i == 5) { expectedBenchedPlayers.add(players.get(i).getId()); }

            if (i==1 || i==3 || i==5) {
                ps.setHowManyTimesOnBench(1);
                ps.setInningPositions(new LinkedList<FieldPositionEnum>(Arrays.asList(FieldPositionEnum.BENCH)));
            } else {
                ps.setHowManyTimesOnBench(0);
                ps.setInningPositions(new LinkedList<FieldPositionEnum>(Arrays.asList(FieldPositionEnum.CATCHER)));
            }

            playerStateMap.put(players.get(i).getId(), ps);
        }

        List<PlayerState> benchedPlayers = gameService.findPrevInningBenchedPlayers(playerStateMap);

        assertEquals(3, benchedPlayers.size());
        for(PlayerState ps : benchedPlayers) {
            int index = expectedBenchedPlayers.lastIndexOf(ps.getPlayer().getId());
            if (index >= 0) {
                expectedBenchedPlayers.remove(index);
            }
        }

        // should have found all the benched players so this list should be empty
        assertEquals(0, expectedBenchedPlayers.size());
    }

    @Test
    public void shufflePlayersTest() {

        Map<Integer, PlayerState> playerStateMap = new HashMap<Integer, PlayerState>();
        List<Integer> players = new ArrayList<>();

        List<PlayerDao> playerList = createPlayers();
        for(PlayerDao pd : playerList) {
            players.add(pd.getId());

            PlayerState ps = new PlayerState();
            ps.setHowManyTimesInPremiumPositions(1);
            ps.setPlayer(pd);
            ps.setHowManyTimesOnBench(0);

            playerStateMap.put(pd.getId(), ps);
        }

        List<PlayerDao> shuffledPlayers =  gameService.shufflePlayers(playerStateMap, players);
        // compare original list with shuffled list
        // they should be different
        assertFalse(Arrays.equals(shuffledPlayers.toArray(), players.toArray()));

    }

    @Test
    public void sortCurrentInningTest() {
        List<FieldPositionEnum> positions = new ArrayList<>();
        positions.addAll(gameService.populatePositions(FieldPositionsConfiguration.premiumPositions));
        positions.addAll(gameService.populatePositions(FieldPositionsConfiguration.infieldPositions));
        positions.addAll(gameService.populatePositions(FieldPositionsConfiguration.outfieldPositions));

        List<PlayerDao> playerList = createPlayers();
        Map<PlayerDao, FieldPositionEnum> inningMap = new HashMap<>();
        for(PlayerDao pd : playerList) {
            if (positions.size() <= 0) {
                inningMap.put(pd, FieldPositionEnum.BENCH);
            } else {
                inningMap.put(pd, positions.get(0));
                positions.remove(0);
            }
        }

        Map<PlayerDao, FieldPositionEnum> sortedMap = gameService.sortCurrentInning(inningMap);
        String previous = ""; // empty string: guaranteed to be less than or equal to any other

        boolean isAlphabetized = true;
        for (final Entry<PlayerDao, FieldPositionEnum> entry: sortedMap.entrySet()) {
            if (entry.getKey().getLastName().compareToIgnoreCase(previous) < 0) {
                isAlphabetized = false;
            }
            previous = entry.getKey().getLastName();
        }

        assertTrue(isAlphabetized);

    }

    @Test
    public void inningCleanupTest() {

        List<FieldPositionEnum> premiumPositions = new ArrayList();
        premiumPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.premiumPositions));

        List<FieldPositionEnum> infieldPositions = new ArrayList();
        infieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.infieldPositions));

        List<FieldPositionEnum> outfieldPositions = new ArrayList();
        outfieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.outfieldPositions));

        List<PlayerDao> playerList = createPlayers();
        Map<PlayerDao, FieldPositionEnum> inningMap = new HashMap<>();
        Map<Integer, PlayerState> playerStateMap = new HashMap<>();
        for(PlayerDao pd : playerList) {
            if (outfieldPositions.size() > 0) {
                inningMap.put(pd, outfieldPositions.get(0));

                PlayerState ps = new PlayerState();
                ps.setInningPositions(new LinkedList<FieldPositionEnum>(Arrays.asList(outfieldPositions.get(0))));
                ps.setPlayer(pd);
                ps.setHowManyTimesInPremiumPositions(0);
                ps.setHowManyTimesOnBench(0);
                playerStateMap.put(pd.getId(), ps);

                outfieldPositions.remove(0);
            } else if (premiumPositions.size() > 0) {
                inningMap.put(pd, premiumPositions.get(0));

                PlayerState ps = new PlayerState();
                ps.setInningPositions(new LinkedList<FieldPositionEnum>(Arrays.asList(premiumPositions.get(0))));
                ps.setPlayer(pd);
                ps.setHowManyTimesInPremiumPositions(1);
                ps.setHowManyTimesOnBench(0);
                playerStateMap.put(pd.getId(), ps);

                premiumPositions.remove(0);
            } else {
                inningMap.put(pd, FieldPositionEnum.BENCH);

                PlayerState ps = new PlayerState();
                ps.setInningPositions(new LinkedList<FieldPositionEnum>(Arrays.asList(FieldPositionEnum.BENCH)));
                ps.setPlayer(pd);
                ps.setHowManyTimesInPremiumPositions(0);
                ps.setHowManyTimesOnBench(1);
                playerStateMap.put(pd.getId(), ps);
            }
        }
        // we only  have premium and outfield positions filled and the remaining players are benched
        // inningCleanup should fix this problem
        gameService.inningCleanup(inningMap,  playerStateMap, outfieldPositions, infieldPositions, premiumPositions);

        int countNumOnBench = 0;
        for(Entry<PlayerDao, FieldPositionEnum> item: inningMap.entrySet()) {
            if (item.getValue() == FieldPositionEnum.BENCH) {
                countNumOnBench++;
            }
        }

        assertEquals(1, countNumOnBench);
        assertEquals(0, outfieldPositions.size());

    }

    @Test
    public void pickPositionFirstInningTest() {

        List<FieldPositionEnum> premiumPositions = new ArrayList();
        List<FieldPositionEnum> premiumPositionsOrig = new ArrayList();
        premiumPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.premiumPositions));
        premiumPositionsOrig.addAll(gameService.populatePositions(FieldPositionsConfiguration.premiumPositions));

        List<FieldPositionEnum> infieldPositions = new ArrayList();
        List<FieldPositionEnum> infieldPositionsOrig = new ArrayList();
        infieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.infieldPositions));
        infieldPositionsOrig.addAll(gameService.populatePositions(FieldPositionsConfiguration.infieldPositions));

        List<FieldPositionEnum> outfieldPositions = new ArrayList();
        List<FieldPositionEnum> outfieldPositionsOrig = new ArrayList();
        outfieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.outfieldPositions));
        outfieldPositionsOrig.addAll(gameService.populatePositions(FieldPositionsConfiguration.outfieldPositions));

        List<PlayerDao> playerList = createPlayers();
        Map<PlayerDao, FieldPositionEnum> inningMap = new HashMap<>();
        Map<Integer, PlayerState> playerStateMap = new HashMap<>();
        for(PlayerDao pd : playerList) {
            PlayerState ps = new PlayerState();
            ps.setPlayer(pd);
            ps.setInningPositions(new LinkedList<FieldPositionEnum>());
            playerStateMap.put(pd.getId(), ps);
        }

        FieldPositionEnum selectedPosition = null;
        for(Entry<Integer, PlayerState> entry : playerStateMap.entrySet()) {
            selectedPosition = gameService.pickPosition(entry.getValue(), outfieldPositions, infieldPositions, premiumPositions);

            if (!outfieldPositionsOrig.isEmpty()) {
                assertTrue(outfieldPositionsOrig.contains(selectedPosition));
                outfieldPositionsOrig.remove(selectedPosition);
            } else if (!premiumPositionsOrig.isEmpty()) {
                assertTrue(premiumPositionsOrig.contains(selectedPosition));
                premiumPositionsOrig.remove(selectedPosition);
            } else if (!infieldPositionsOrig.isEmpty()) {
                assertTrue(infieldPositionsOrig.contains(selectedPosition));
                infieldPositionsOrig.remove(selectedPosition);
            } else {
                assertEquals(FieldPositionEnum.BENCH, selectedPosition);
            }
        }
    }

    @Test
    public void pickPositionThirdInningTest() {
        List<FieldPositionEnum> premiumPositions = new ArrayList();
        premiumPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.premiumPositions));

        List<FieldPositionEnum> infieldPositions = new ArrayList();
        infieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.infieldPositions));

        List<FieldPositionEnum> outfieldPositions = new ArrayList();
        outfieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.outfieldPositions));

        PlayerDao pd = new PlayerDao();
        pd.setId(1);
        pd.setLastName("Wayne");
        pd.setFirstName("Bruce");
        pd.setTimesInPremium(1);
        PlayerState ps = new PlayerState();
        ps.setPlayer(pd);
        ps.setHowManyTimesOnBench(0);
        LinkedList previousPositions = new LinkedList<FieldPositionEnum>();
        previousPositions.push(FieldPositionEnum.CF);
        previousPositions.push(FieldPositionEnum.FIRSTB);
        ps.setInningPositions(previousPositions);

        // expecting next position to be outfield
        FieldPositionEnum selectedPosition = gameService.pickPosition(ps, outfieldPositions, infieldPositions, premiumPositions);

        assertTrue(FieldPositionsConfiguration.outfieldPositions.contains(selectedPosition));

    }

    @Test
    public void pickPositionAnotherInningTest() {
        List<FieldPositionEnum> premiumPositions = new ArrayList();
        premiumPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.premiumPositions));

        List<FieldPositionEnum> infieldPositions = new ArrayList();
        infieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.infieldPositions));

        List<FieldPositionEnum> outfieldPositions = new ArrayList();
        outfieldPositions.addAll(gameService.populatePositions(FieldPositionsConfiguration.outfieldPositions));

        PlayerDao pd = new PlayerDao();
        pd.setId(1);
        pd.setLastName("Wayne");
        pd.setFirstName("Bruce");
        pd.setTimesInPremium(1);
        PlayerState ps = new PlayerState();
        ps.setPlayer(pd);
        ps.setHowManyTimesOnBench(0);
        LinkedList previousPositions = new LinkedList<FieldPositionEnum>();
        previousPositions.push(FieldPositionEnum.CF);
        previousPositions.push(FieldPositionEnum.FIRSTB);
        previousPositions.push(FieldPositionEnum.BENCH);
        previousPositions.push(FieldPositionEnum.LF);
        ps.setInningPositions(previousPositions);

        // expecting next position to be infield
        FieldPositionEnum selectedPosition = gameService.pickPosition(ps, outfieldPositions, infieldPositions, premiumPositions);

        assertTrue(FieldPositionsConfiguration.infieldPositions.contains(selectedPosition));

    }

    @Test
    public void initializePlayerStateTest() {
        PlayerDao player = new PlayerDao();
        player.setFirstName("Mickey");
        player.setLastName("Mouse");

        PlayerState ps = gameService.initializePlayerState(player);

        assertTrue(ps.getPlayer().getLastName().compareTo(player.getLastName()) == 0);
        assertTrue(ps.getPlayer().getFirstName().compareTo(player.getFirstName()) == 0);
        assertEquals(0, ps.getHowManyTimesInPremiumPositions());
        assertEquals(0, ps.getInningPositions().size());

    }

    private List<PlayerDao> createPlayers() {
        List<PlayerDao> players = new ArrayList<>();

        PlayerDao player = new PlayerDao();
        player.setId(0);
        player.setFirstName("Mickey");
        player.setLastName("Mouse");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(1);
        player.setFirstName("Donald");
        player.setLastName("Duck");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(2);
        player.setFirstName("Mary");
        player.setLastName("Poppins");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(3);
        player.setFirstName("Winnie");
        player.setLastName("Pooh");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(4);
        player.setFirstName("Cinder");
        player.setLastName("Ella");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(5);
        player.setFirstName("Peter");
        player.setLastName("Pan");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(6);
        player.setFirstName("Captain");
        player.setLastName("Hook");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(7);
        player.setFirstName("Mighty");
        player.setLastName("Mouse");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(8);
        player.setFirstName("Papa");
        player.setLastName("Smurf");
        player.setTimesInPremium(1);
        players.add(player);

        player = new PlayerDao();
        player.setId(9);
        player.setFirstName("Rip");
        player.setLastName("van Winkle");
        player.setTimesInPremium(1);
        players.add(player);

        return players;
    }
}



