package com.baseballPlanner.tx.dao;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import org.springframework.stereotype.Component;

/**
 * Created by aziring on 5/9/17.
 */
@Entity
@Table(name = "INNING")
public class InningDao implements Serializable {

    private InningId inningId;
    private Integer inningNumber;
    private String fieldPosition;
    private GameDao game;
    private PlayerDao player;

    @EmbeddedId
    public InningId getInningId() { return inningId; }

    public void setInningId(InningId inningId) { this.inningId = inningId;  }

    @MapsId("gameId")
    @ManyToOne
    @JoinColumn(name = "GAME_ID", referencedColumnName = "id")
    public GameDao getGame() {
        return this.game;
    }

    public void setGame(GameDao game) {
        this.game = game;
    }

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", referencedColumnName = "id")
    public PlayerDao getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerDao player) {
        this.player = player;
    }



    @Column(name = "INNING_NUMBER")
    public Integer getInningNumber() {
        return inningNumber;
    }

    public void setInningNumber(Integer inningNumber) {
        this.inningNumber = inningNumber;
    }

    @Column(name = "FIELD_POSITION")
    public String getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(String fieldPosition) {
        this.fieldPosition = fieldPosition;
    }
}
