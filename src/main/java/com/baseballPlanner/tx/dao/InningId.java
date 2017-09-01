package com.baseballPlanner.tx.dao;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by aziring on 7/17/17.
 */
@Embeddable
public class InningId implements Serializable{

  @Column(name = "INNING_NUMBER")
  private int inningNumber;
  @Column(name = "GAME_ID")
  private int gameId;
  @Column(name = "PLAYER_ID")
  private int playerId;

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public int getPlayerId() {
    return playerId;
  }

  public void setPlayerId(int playerId) {
    this.playerId = playerId;
  }

  public int getInningNumber() {
    return inningNumber;
  }

  public void setInningNumber(int inningNumber) {
    this.inningNumber = inningNumber;
  }

}
