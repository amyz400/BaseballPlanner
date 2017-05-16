package com.baseballPlanner.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aziring on 5/9/17.
 */

public class FieldPositionsConfiguration {

    public static List<FieldPositionEnum> infieldPositions = new ArrayList<>();
    public static List<FieldPositionEnum> outfieldPositions = new ArrayList<>();
    public static List<FieldPositionEnum> premiumPositions = new ArrayList<>();

    public static void populateFieldPositions(List<FieldPositionEnum> inInfieldPositions,
                                              List<FieldPositionEnum> inOutfieldPositions,
                                              List<FieldPositionEnum> inPremiumPositions) {
        infieldPositions.addAll(inInfieldPositions);
        outfieldPositions.addAll(inOutfieldPositions);
        premiumPositions.addAll(inPremiumPositions);
    }

    public static void addInfieldPosition(FieldPositionEnum fieldPosition) {
        infieldPositions.add(fieldPosition);
    }

    public static void addOutfieldPosition(FieldPositionEnum fieldPosition) {
        outfieldPositions.add(fieldPosition);
    }

    public static void addPremiumPosition(FieldPositionEnum fieldPosition) {
        premiumPositions.add(fieldPosition);
    }

}
