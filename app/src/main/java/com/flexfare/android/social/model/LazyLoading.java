package com.flexfare.android.social.model;

import com.flexfare.android.social.enums.ItemType;

/**
 * Created by kodenerd on 10/9/17.
 */

public interface LazyLoading {
    ItemType getItemType();
    void setItemType(ItemType itemType);
}
