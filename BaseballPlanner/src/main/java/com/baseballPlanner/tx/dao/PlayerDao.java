package com.baseballPlanner.tx.dao;

import javax.ws.rs.DefaultValue;

/**
 * Created by aziring on 5/9/17.
 */
public class PlayerDao {

    private int id;
    private String lastName;
    private String firstName;

    @DefaultValue("1")
    private int timesInPremium;


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
