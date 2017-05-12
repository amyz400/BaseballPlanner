package com.baseballPlanner.models;

import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.PlayerDao;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by aziring on 5/9/17.
 */
public class GameModel {

    private int id;
    private LocalDateTime datePlayed;
    private LinkedList<List<InningDao>> inningList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(LocalDateTime datePlayed) {
        this.datePlayed = datePlayed;
    }

    public LinkedList<List<InningDao>> getInningList() {
        return inningList;
    }

    public void setInningList(LinkedList<List<InningDao>> inningList) {
        this.inningList = inningList;
    }
}
