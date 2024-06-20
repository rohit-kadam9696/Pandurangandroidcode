package com.twd.chitboyapp.spsskl.constant;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class LocationCalculator {
    public double distanceBetween2LatLngs(LatLng latLng1, LatLng latLng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);
        Location loc2 = new Location("");
        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);
        return loc1.distanceTo(loc2);
    }
}
