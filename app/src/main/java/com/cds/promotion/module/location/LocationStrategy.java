package com.cds.promotion.module.location;

public interface LocationStrategy {

    void requestLocation();

    void stopLocation();

    void setListener(UpdateLocationListener listener);
}
