package com.baseballPlanner.service;

import com.baseballPlanner.tx.dao.PlayerDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amy on 5/11/17.
 */
public interface PlayerRepo extends CrudRepository<PlayerDao, Integer> {

}
