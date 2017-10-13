package com.flexfare.flextools;

/**
 * Created by kodenerd on 8/19/17.
 */

public enum ErrorType {
    EMPTY,
    PATTERN,
    VALUE,
    CHARS,
    NONE;

    public static ErrorType valueOf(int value) {
        for (ErrorType errorType : ErrorType.values()) {
            if (value == errorType.ordinal()) {
                return errorType;
            }
        }
        return NONE;
    }

    @Override
    public String toString() {
        switch (this) {
            case EMPTY:
                return "Empty";

            case PATTERN:
                return "Pattern";

            case VALUE:
                return "Value";

            case CHARS:
                return "Chars";

            default:
                return "None";
        }
    }
}
