package com.quickserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.quickserve.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);
}