package com.baseballPlanner.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aziring on 5/9/17.
 */
public class FieldPositionsConfiguration {

    private static List<FieldPositionEnum> infieldPositions = new ArrayList<>();
    private static List<FieldPositionEnum> outfieldPositions = new ArrayList<>();
    private static List<FieldPositionEnum> premiumPositions = new ArrayList<>();
    private static List<FieldPositionEnum> miscPositions = new ArrayList<>();

    public static void populateFieldPositions(List<FieldPositionEnum> inInfieldPositions,
                                              List<FieldPositionEnum> inOutfieldPositions,
                                              List<FieldPositionEnum> inPremiumPositions,
                                              List<FieldPositionEnum> inMiscPositions) {
        infieldPositions.addAll(inInfieldPositions);
        outfieldPositions.addAll(inOutfieldPositions);
        premiumPositions.addAll(inPremiumPositions);
        miscPositions.addAll(inMiscPositions);
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

    public static void addMiscPosition(FieldPositionEnum fieldPosition) {
        miscPositions.add(fieldPosition);
    }

}
