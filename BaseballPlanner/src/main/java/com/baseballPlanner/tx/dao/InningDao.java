package com.baseballPlanner.tx.dao;

/**
 * Created by aziring on 5/9/17.
 */
public class InningDao {

    private int id;
    private GameDao game;
    private Integer inningNumber;
    private PlayerDao player;
    private String fieldPosition;


    public int getId() {
        return id;
    }

    public GameDao getGame() {
        return game;
    }

    public void setGame(GameDao game) {
        this.game = game;
    }

    public Integer getInningNumber() {
        return inningNumber;
    }

    public void setInningNumber(Integer inningNumber) {
        this.inningNumber = inningNumber;
    }

    public String getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(String fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public PlayerDao getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDao player) {
        this.player = player;
    }
}
