package com.baseballPlanner.service.repositories;

import com.baseballPlanner.tx.dao.GameDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by aziring on 7/11/17.
 */
@Repository("GameRepo")
public interface GameRepo extends CrudRepository<GameDao, Integer> {

}
