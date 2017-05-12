package com.baseballPlanner.tx.dao;


import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by aziring on 5/9/17.
 */
public class PlayerDao {

    private int id;
    private String lastName;
    private String firstName;
    private int timesInPremium = 1;

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getTimesInPremium() {
        return timesInPremium;
    }

    public void setTimesInPremium(int timesInPremium) {
        this.timesInPremium = timesInPremium;
    }
}
