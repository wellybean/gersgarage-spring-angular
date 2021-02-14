package com.wellybean.gersgarage.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.wellybean.gersgarage.dto.BookingDTO;
import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.security.service.UserDetailsImpl;
import com.wellybean.gersgarage.service.BookingService;
import com.wellybean.gersgarage.service.ServiceService;
import com.wellybean.gersgarage.service.UserService;
import com.wellybean.gersgarage.service.VehicleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
@PreAuthorize("hasRole('USER')")
public class BookingController {

    private static final Logger LOGGER = LogManager.getLogger(BookingController.class);

    private final VehicleService vehicleService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ServiceService serviceService;

    @Autowired
    public BookingController(final VehicleService vehicleService,
                             final UserService userService,
                             final BookingService bookingService,
                             final ServiceService serviceService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<?> getAllBookingsForUser() {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        LOGGER.info("[BOOKING-CONTROL] - Going to get all bookings for user {}.", username);
        Optional<List<Booking>> bookingsList = bookingService.getAllBookingsForUser(username);

        if(bookingsList.isPresent()) {
            List<BookingDTO> dtoList = bookingsList.get().stream().map(BookingDTO::new).collect(Collectors.toList());
            LOGGER.info("[BOOKING-CONTROL] - Returning {} booking(s) found for user {}.", dtoList.size(), username);
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        }
        LOGGER.info("[BOOKING-CONTROL] - No bookings found for user {}.", username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<?> book(@RequestBody final Booking booking) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDetailsImpl) loggedInUser.getPrincipal()).getId();
        Optional<User> userOptional = userService.findById(userId);
        Optional<Vehicle> vehicleOptional = vehicleService.getVehicle(booking.getVehicle().getId());
        Optional<Service> serviceOptional = serviceService.findById(booking.getService().getId());
        if(userOptional.isPresent() && vehicleOptional.isPresent() && serviceOptional.isPresent()) {
            booking.setUser(userOptional.get());
            booking.setService(serviceOptional.get());
            booking.setVehicle(vehicleOptional.get());
            Booking savedBooking = bookingService.makeBooking(booking);
            return new ResponseEntity<>(new BookingDTO(savedBooking), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        Optional<Booking> bookingOptional = bookingService.getBooking(id);
        if(bookingOptional.isPresent()) {
            bookingService.deleteBooking(bookingOptional.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
