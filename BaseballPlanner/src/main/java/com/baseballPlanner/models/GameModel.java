package com.baseballPlanner.models;

import com.baseballPlanner.tx.dao.PlayerDao;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by aziring on 5/9/17.
 */
public class GameModel {

    private int id;
    private LocalDateTime datePlayed;

    Map<Integer, Map<PlayerDao, FieldPositionEnum>> inningMap;
}
