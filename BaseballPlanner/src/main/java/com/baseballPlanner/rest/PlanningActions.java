package com.baseballPlanner.rest;

import com.baseballPlanner.models.GameModel;
import com.baseballPlanner.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Created by amy on 5/10/17.
 */
@RestController
public class PlanningActions {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check() {
        return "SUCCESS";
    }

    @RequestMapping(value = "/createGame", method = RequestMethod.GET)
    public String createGame(@RequestParam(required = false) LocalDate datePlayed) {

        if (null == datePlayed) {
            return gameService.createGame(LocalDate.now()).formatGame();
        } else {
            return gameService.createGame(datePlayed).formatGame();
        }
    }
}