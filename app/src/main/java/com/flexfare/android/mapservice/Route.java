package com.flexfare.android.mapservice;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by kodenerd on 8/25/17.
 */

public class Route {

    public Distance distance;
    public Duration  duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
