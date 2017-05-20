package com.baseballPlanner.service;

import com.baseballPlanner.tx.dao.PlayerDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by amy on 5/11/17.
 */
@Repository("playerRepo")
public interface PlayerRepo extends CrudRepository<PlayerDao, Integer> {

}
