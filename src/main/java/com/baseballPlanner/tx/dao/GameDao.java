package com.baseballPlanner.tx.dao;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by aziring on 5/9/17.
 */
public class GameDao implements Serializable{

    private int id;
    private LocalDateTime datePlayed;
    private List<InningDao> innings;

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public LocalDateTime getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(LocalDateTime datePlayed) {
        this.datePlayed = datePlayed;
    }

    public List<InningDao> getInnings() {
        return innings;
    }

    public void setInnings(List<InningDao> innings) {
        this.innings = innings;
    }
}
