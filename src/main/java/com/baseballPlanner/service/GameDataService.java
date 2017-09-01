package com.baseballPlanner.service;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.GameModel;
import com.baseballPlanner.service.repositories.GameRepo;
import com.baseballPlanner.service.repositories.InningRepo;
import com.baseballPlanner.tx.dao.GameDao;
import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.InningId;
import com.baseballPlanner.tx.dao.PlayerDao;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by aziring on 7/11/17.
 */
@Service("gameDataService")
public class GameDataService {

  @Autowired
  private GameRepo gameRepo;

  @Autowired
  private InningRepo inningRepo;

  public GameDao saveGame(GameModel gameModel) {
    int id = -1;

    GameDao gameDao = new GameDao();
    gameDao.setDatePlayed(LocalDate.now());
    GameDao gd = gameRepo.save(gameDao);

    // create innings
    for (Entry<Integer, Map<PlayerDao, FieldPositionEnum>> entry : gameModel.getInningMap().entrySet()){
      for (Entry<PlayerDao, FieldPositionEnum> playerInfo : entry.getValue().entrySet()) {
        InningDao inning = new InningDao();
        InningId inningId = new InningId();
        inningId.setPlayerId(playerInfo.getKey().getId());
        inningId.setGameId(gameDao.getId());
        inningId.setInningNumber(entry.getKey());
        inning.setInningId(inningId);
        inning.setFieldPosition(playerInfo.getValue().toString());
        inning.setGame(gameDao);
        inning.setPlayer(playerInfo.getKey());
        inningRepo.save(inning);
      }
    }

    return gd;
  }

}
