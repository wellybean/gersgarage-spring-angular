package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Optional<List<Booking>> getAllBookingsForUser(String username);
    Optional<List<Booking>> getAllBookingsForDate(LocalDate date);
    Booking makeBooking(Booking booking);
    void deleteBooking(Booking booking);
    Optional<Booking> getBooking(Long id);
}
