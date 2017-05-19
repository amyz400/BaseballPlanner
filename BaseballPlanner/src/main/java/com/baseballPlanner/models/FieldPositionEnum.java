package com.baseballPlanner.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aziring on 5/9/17.
 */
public enum FieldPositionEnum {

    LF ("Left Field"),
    RF ("Right Field"),
    CF ("Center Field"),
    FIRSTB ("First Base"),
    SECONDB ("Second Base"),
    THIRDB ("Third Base"),
    SS ("Shortstop"),
    CATCHER ("Catcher"),
    PITCHER ("Pitcher"),
    BENCH ("Bench");

    private final String value;
    private static final Map<String, FieldPositionEnum> enumMap = new HashMap<>();

    public String getValue() { return value; }

    FieldPositionEnum(String value) {
        this.value = value;
    }

    public static FieldPositionEnum enumByValue(String value) {
        return enumMap.get(value);
    }

    static {
        for (FieldPositionEnum dte : FieldPositionEnum.values()) {
            enumMap.put(dte.getValue(), dte);
        }
    }
}
