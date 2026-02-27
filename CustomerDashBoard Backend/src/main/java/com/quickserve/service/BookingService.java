package com.quickserve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.quickserve.model.Booking;
import com.quickserve.model.ServiceEntity;
import com.quickserve.repository.BookingRepository;
import com.quickserve.repository.ServiceRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private NotificationService notificationService;

    // ---------------- SAVE BOOKING ----------------
    public Booking saveBooking(Booking booking) {

        booking.setStatus("BOOKED");
        Booking savedBooking = bookingRepository.save(booking);

        notificationService.createNotification(
                booking.getUserId(),
                "Your booking has been successfully created."
        );

        return savedBooking;
    }
    // ---------------- GET USER BOOKINGS ----------------
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // ---------------- GET ALL BOOKINGS ----------------
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // ---------------- UPDATE BOOKING ----------------
    public Booking updateBooking(Long id, Booking updatedBooking) {

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Fetch full service again
        Long serviceId = updatedBooking.getService().getId();

        ServiceEntity fullService = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        existingBooking.setUserId(updatedBooking.getUserId());
        existingBooking.setService(fullService);
        existingBooking.setBookingDate(updatedBooking.getBookingDate());
        existingBooking.setTimeSlot(updatedBooking.getTimeSlot());
        existingBooking.setStatus(updatedBooking.getStatus());

        return bookingRepository.save(existingBooking);
    }

    // ---------------- CANCEL BOOKING ----------------
    public void cancelBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("CANCELLED");

        bookingRepository.save(booking);
    }
}