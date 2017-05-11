package com.baseballPlanner.rest;

import com.baseballPlanner.models.GameModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by amy on 5/10/17.
 */
@RestController
public class PlanningActions {

    @RequestMapping(value = "/planning", method = RequestMethod.GET)
    public GameModel createGame() {
        return null; //userService.save(user);
    }
}
