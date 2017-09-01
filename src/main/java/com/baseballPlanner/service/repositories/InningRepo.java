package com.baseballPlanner.service.repositories;

import com.baseballPlanner.tx.dao.InningDao;
import com.baseballPlanner.tx.dao.InningId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by aziring on 7/11/17.
 */
@Repository
public interface InningRepo extends CrudRepository<InningDao, InningId> {

}
