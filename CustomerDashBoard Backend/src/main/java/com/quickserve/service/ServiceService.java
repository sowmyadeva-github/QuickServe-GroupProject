package com.quickserve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.quickserve.model.ServiceEntity;
import com.quickserve.model.NearbyServiceResponse;
import com.quickserve.repository.ServiceRepository;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceEntity addService(ServiceEntity service) {
        return serviceRepository.save(service);
    }

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<ServiceEntity> getAvailableServices() {
        return serviceRepository.findByAvailableTrue();
    }

    private double calculateDistance(double lat1, double lon1,
                                     double lat2, double lon2) {

        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public List<NearbyServiceResponse> findNearbyServices(double userLat,
                                                          double userLon,
                                                          double distance,
                                                          String unit) {

        double radiusKm = unit.equalsIgnoreCase("meters") ? distance / 1000.0 : distance;

        List<ServiceEntity> allServices = serviceRepository.findByAvailableTrue();

        return allServices.stream()
                .filter(service -> service.getLatitude() != null
                        && service.getLongitude() != null)
                .map(service -> {

                    double calculatedDistance = calculateDistance(
                            userLat,
                            userLon,
                            service.getLatitude(),
                            service.getLongitude());

                    return new NearbyServiceResponse(service, calculatedDistance);
                })
                .filter(response -> response.getDistance() <= radiusKm)
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .collect(Collectors.toList());
    }
}