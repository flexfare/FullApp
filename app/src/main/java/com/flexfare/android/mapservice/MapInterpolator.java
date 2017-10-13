package com.flexfare.android.mapservice;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kodenerd on 8/28/17.
 */

public interface MapInterpolator {

    LatLng interpolate(float fraction, LatLng pointOne, LatLng pointTwo);

   class MapCalc implements MapInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng pointOne, LatLng pointTwo) {
            double lat = (pointTwo.latitude - pointTwo.latitude) * fraction + pointOne.latitude;
            double lngDouble = pointTwo.longitude - pointOne.longitude;
            if (Math.abs(lngDouble) > 100) {
                lngDouble -= Math.signum(lngDouble) * 360;
            }
            double lng = lngDouble * fraction + pointOne.longitude;
            return new LatLng(lat,lng);
        }
    }
}
