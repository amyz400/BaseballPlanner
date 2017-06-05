package com.baseballPlanner.models;

import com.baseballPlanner.tx.dao.PlayerDao;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by aziring on 5/21/17.
 */
public class PlayerState implements Serializable, Comparable<PlayerState>{

    private int howManyTimesInPremiumPositions;
    private int howManyTimesOnBench;
    private LinkedList<FieldPositionEnum> inningPositions;
    private PlayerDao player;

    @Override
    public int compareTo( final PlayerState p) {
        int retVal = p.getHowManyTimesOnBench() - howManyTimesOnBench;
        return retVal;
    }

    public void addToHowManyTimesInPremiumPositions(int i) {

        this.howManyTimesInPremiumPositions += i;
    }

    public void addToHowManyTimesOnBench(int i) {

        this.howManyTimesOnBench += i;
    }

    public void deleteFromHowManyTimesOnBench(int i) {
        this.howManyTimesOnBench -= i;
    }

    public int getHowManyTimesInPremiumPositions() {
        return howManyTimesInPremiumPositions;
    }

    public void setHowManyTimesInPremiumPositions(int howManyTimesInPremiumPositions) {
        this.howManyTimesInPremiumPositions = howManyTimesInPremiumPositions;
    }

    public int getHowManyTimesOnBench() { return howManyTimesOnBench; }

    public void setHowManyTimesOnBench(int howManyTimesOnBench) { this.howManyTimesOnBench = howManyTimesOnBench; }

    public LinkedList<FieldPositionEnum> getInningPositions() {
        return inningPositions;
    }

    public void setInningPositions(LinkedList<FieldPositionEnum> inningPositions) {
        this.inningPositions = inningPositions;
    }

    public PlayerDao getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDao player) {
        this.player = player;
    }
}
