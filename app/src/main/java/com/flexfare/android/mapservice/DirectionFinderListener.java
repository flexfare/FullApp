package com.flexfare.android.mapservice;

import java.util.List;

/**
 * Created by kodenerd on 8/25/17.
 */

public interface DirectionFinderListener {

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
