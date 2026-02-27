package com.quickserve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.quickserve.model.ServiceEntity;
import com.quickserve.model.NearbyServiceResponse;
import com.quickserve.service.ServiceService;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @PostMapping
    public ServiceEntity addService(@RequestBody ServiceEntity service) {
        return serviceService.addService(service);
    }

    @GetMapping
    public List<ServiceEntity> getAllServices() {
        return serviceService.getAllServices();
    }

    @GetMapping("/available")
    public List<ServiceEntity> getAvailableServices() {
        return serviceService.getAvailableServices();
    }

    @GetMapping("/nearby")
    public List<NearbyServiceResponse> getNearbyServices(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double distance,
            @RequestParam String unit) {

        return serviceService.findNearbyServices(lat, lon, distance, unit);
    }
}