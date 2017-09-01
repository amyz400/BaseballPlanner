package com.baseballPlanner.tx.dao;


import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by aziring on 5/9/17.
 */
@Entity
@Table(name = "GAME")
public class GameDao implements Serializable{

    private int id;
    private LocalDate datePlayed;
    private List<InningDao> innings;

    public GameDao() {
        innings = new ArrayList<InningDao>();
    }

    public GameDao(LocalDate datePlayed, List<InningDao> innings) {
        this();
        this.datePlayed = datePlayed;
        this.getInnings().addAll(innings);
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    @Column(name = "DATE_PLAYER",nullable = false)
    public LocalDate getDatePlayed() {

        return datePlayed;
    }

    public void setDatePlayed(LocalDate datePlayed) {

        this.datePlayed = datePlayed;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<InningDao> getInnings() {
        return innings;
    }

    public void setInnings(List<InningDao> innings) {
        this.innings = innings;
    }
}
