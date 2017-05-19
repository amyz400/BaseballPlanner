package com.baseballPlanner.models;

import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.PlayerDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by aziring on 5/9/17.
 */
public class GameModel {

    private int id;
    private LocalDate datePlayed;
    private Map<Integer, Map<PlayerDao, FieldPositionEnum>> inningMap;

    public GameModel() {
        this.inningMap = new HashMap<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(LocalDate datePlayed) {
        this.datePlayed = datePlayed;
    }

    public Map<Integer, Map<PlayerDao, FieldPositionEnum>> getInningMap() {
        return inningMap;
    }

    public void setInningList(Map<Integer,  Map<PlayerDao, FieldPositionEnum>> inningMap) {
        this.inningMap = inningMap;
    }

    public String formatGame() {
        StringBuffer gameResults = new StringBuffer();

        gameResults.append("Game Date: ").append(datePlayed.toString());
        gameResults.append("\n");

        for (Map.Entry<Integer, Map<PlayerDao, FieldPositionEnum>> entry : inningMap.entrySet()) {
            gameResults.append("\n").append("Inning: ").append(entry.getKey()).append("\n");
            for (Map.Entry<PlayerDao, FieldPositionEnum> playerEntry : entry.getValue().entrySet()) {
                gameResults.append("Position: ").append(playerEntry.getValue())
                        .append(" Player: ").append(playerEntry.getKey().getLastName()).append(", ").append(playerEntry.getKey().getFirstName()).append("\n");
            }
        }
        return gameResults.toString();
    }
}
