package com.baseballPlanner.service;

import com.baseballPlanner.models.FieldPositionEnum;
import com.baseballPlanner.models.GameModel;
import com.baseballPlanner.service.repositories.GameRepo;
import com.baseballPlanner.tx.dao.GameDao;
import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.InningId;
import com.baseballPlanner.tx.dao.PlayerDao;
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

  public GameDao saveGame(GameModel gameModel) {
    int id = -1;

    GameDao gameDao = new GameDao();
    gameDao.setDatePlayed(LocalDateTime.now());

    // create innings
    for (Entry<Integer, Map<PlayerDao, FieldPositionEnum>> entry : gameModel.getInningMap().entrySet()){
      for (Entry<PlayerDao, FieldPositionEnum> playerInfo : entry.getValue().entrySet()) {
        InningDao inning = new InningDao();
        InningId inningId = new InningId();
        inningId.setPlayerId(playerInfo.getKey().getId());
        inningId.setGameId(gameDao.getId());
        inning.setInningId(inningId);
        inning.setInningNumber(entry.getKey());
        inning.setFieldPosition(playerInfo.getValue().toString());
        gameDao.getInnings().add(inning);
      }
    }

    return gameRepo.save(gameDao);
  }

}
