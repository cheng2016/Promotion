package com.cds.promotion.service.message;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/23 18:38
 * @Version: 3.0.0
 */
public class LocationMsg {
    private String longitude;

    private String latitude;

    private String deviceTime;

    public LocationMsg() {
    }

    public LocationMsg(String longitude, String latitude, String deviceTime) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.deviceTime = deviceTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDeviceTime() {
        return deviceTime;
    }

    public void setDeviceTime(String deviceTime) {
        this.deviceTime = deviceTime;
    }
}
