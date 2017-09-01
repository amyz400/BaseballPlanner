package com.baseballPlanner.tx.dao;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by aziring on 5/9/17.
 */
@Entity
@Table(name = "PLAYER")
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

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    @Column(name = "LAST_NAME",nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "FIRST_NAME",nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "TIMES_IN_PREMIUM", nullable = false)
    public int getTimesInPremium() {
        return timesInPremium;
    }

    public void setTimesInPremium(int timesInPremium) {
        this.timesInPremium = timesInPremium;
    }
}
