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

/**
 * Service for CRUD operations in Booking entity
 */
@Service public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for class BookingServiceImpl
     * @param bookingRepository  booking repository interface
     * @param userRepository  user repository interface
     */
    @Autowired public BookingServiceImpl(final BookingRepository bookingRepository,
                                         final UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    /**
     * Fetches all bookings for a user
     * @param username user's username
     * @return optional of list of bookings for user
     */
    @Transactional @Override public Optional<List<Booking>> getAllBookingsForUser(String username) {
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

    /**
     * Fetches all bookings for a specific date
     * @param date  date
     * @return optional of list of bookings
     */
    @Override public Optional<List<Booking>> getAllBookingsForDate(LocalDate date) {
        return bookingRepository.findAllByDate(date);
    }

    /**
     * Saves booking info into database
     * @param booking  booking info with required fields
     * @return persisted booking
     */
    @Override public Booking makeBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    /**
     * Removes booking
     * @param booking  booking
     */
    @Override public void deleteBooking(Booking booking) {
        bookingRepository.delete(booking);
    }

    /**
     * Fetches booking info from id
     * @param id  booking id
     * @return booking info
     */
    @Override public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }
}
