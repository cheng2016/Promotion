package com.cds.promotion.module.location;

import android.location.Location;

public interface UpdateLocationListener {
    void updateLocationChanged(Location location, int gpsCount);
}
