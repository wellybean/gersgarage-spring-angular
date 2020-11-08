package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    Optional<List<Booking>> getAllBookingsForUser(String username);
}
