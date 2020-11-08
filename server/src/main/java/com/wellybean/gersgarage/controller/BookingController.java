package com.wellybean.gersgarage.controller;

import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.service.BookingService;
import com.wellybean.gersgarage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
@PreAuthorize("hasRole('USER')")
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> getAllBookingsForUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        Optional<List<Booking>> bookingsList = bookingService.getAllBookingsForUser(username);

        if(bookingsList.isPresent()) {
            return new ResponseEntity<>(bookingsList.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
