package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.repository.BookingRepository;
import com.wellybean.gersgarage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<List<Booking>> getAllBookingsForUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            Optional<List<Booking>> bookings = bookingRepository.findAllByUser(userOptional.get());
            if(bookings.isPresent()){
                return bookings;
            }
        }
        return Optional.empty();
    }
}
