package com.flexfare.android.social.managers.listeners;

import java.util.List;

/**
 * Created by kodenerd on 10/9/17.
 */

public interface OnDataChangedListener<T> {

    public void onListChanged(List<T> list);
}
