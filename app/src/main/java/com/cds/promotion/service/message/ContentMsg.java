package com.cds.promotion.service.message;

import java.util.List;

public class ContentMsg {
    private String title;

    private DetailMsg detail;

    private String tail;

    private String details;

    private List<LocationMsg> locations;

    public List<LocationMsg> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationMsg> locations) {
        this.locations = locations;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DetailMsg getDetail() {
        return detail;
    }

    public void setDetail(DetailMsg detail) {
        this.detail = detail;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
