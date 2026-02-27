package com.quickserve.model;

public class NearbyServiceResponse {

    private ServiceEntity service;
    private double distance;

    public NearbyServiceResponse(ServiceEntity service, double distance) {
        this.service = service;
        this.distance = distance;
    }

    public ServiceEntity getService() {
        return service;
    }

    public double getDistance() {
        return distance;
    }
}