package com.flexfare.android.helper;

/**
 * Created by kodenerd on 9/22/17.
 */

public enum ItemType {LOAD(10), ITEM(11);
    private final int typeCode;

    ItemType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return this.typeCode;
    }
}
