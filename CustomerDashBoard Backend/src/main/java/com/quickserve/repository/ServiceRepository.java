package com.quickserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.quickserve.model.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    List<ServiceEntity> findByAvailableTrue();

    List<ServiceEntity> findByCategory(String category);

    List<ServiceEntity> findByLocation(String location);

    List<ServiceEntity> findByServiceNameContainingIgnoreCase(String keyword);
}