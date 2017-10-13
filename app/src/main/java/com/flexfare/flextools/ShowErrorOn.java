package com.flexfare.flextools;

/**
 * Created by kodenerd on 8/19/17.
 */

public enum ShowErrorOn {

    UNFOCUS,
    CHANGE;

    public static ShowErrorOn valueOf(int value) {
        return value == UNFOCUS.ordinal() ? UNFOCUS : CHANGE;
    }
    @Override
    public String toString() {
        return this == CHANGE ? "Change" : "Unfocus";
    }
}
