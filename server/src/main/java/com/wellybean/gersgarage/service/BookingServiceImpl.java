package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.repository.BookingRepository;
import com.wellybean.gersgarage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Optional<List<Booking>> getAllBookingsForUser(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            Optional<List<Booking>> bookings = bookingRepository.findAllByUser(userOptional.get());
            if(bookings.isPresent()){
                // This is necessary so that the bookings list is initialised in this transactional context and a LazyInitializationException is avoided
                bookings.get().forEach(booking -> {
                    booking.getVehicle();
                    booking.getService();
                    booking.getUser();
                });
                return bookings;
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<Booking>> getAllBookingsForDate(LocalDate date) {
        return bookingRepository.findAllByDate(date);
    }

    @Override
    public Booking makeBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Booking booking) {
        bookingRepository.delete(booking);
    }

    @Override
    public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }
}
