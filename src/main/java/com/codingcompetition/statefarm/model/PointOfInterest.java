package com.codingcompetition.statefarm.model;

import java.util.HashMap;
import java.util.Map;

public class PointOfInterest {

    private Map<Object,String> descriptors;
    private String latitude;
    private String longitude;

    public PointOfInterest() {
        descriptors = new HashMap<>();
    }

    public PointOfInterest(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Map<Object,String> getDescriptors() {
        return this.descriptors;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void addDescriptor(Object key, String value) {
        descriptors.put(key, value);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
