package com.baseballPlanner.tx.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aziring on 5/9/17.
 */
public class PlayerDao implements Serializable, Comparable<PlayerDao>{

    private int id;
    private String lastName;
    private String firstName;
    private int timesInPremium = 1;

    @Override
    public int compareTo( final PlayerDao p) {
        String name = lastName + ", " + firstName;
        String compareName = p.getLastName() + ", " + p.getFirstName();
        int retVal = name.compareTo(compareName);
        return retVal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

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
