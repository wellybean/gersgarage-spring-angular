package com.wellybean.gersgarage.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.service.AvailabilityService;
import com.wellybean.gersgarage.service.ServiceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
@PreAuthorize("hasRole('USER')")
public class AvailabilityController {

    private static final Logger LOGGER = LogManager.getLogger(AvailabilityController.class);

    private final AvailabilityService availabilityService;
    private final ServiceService serviceService;

    @Autowired public AvailabilityController(final AvailabilityService availabilityService, final ServiceService serviceService) {
        this.availabilityService = availabilityService;
        this.serviceService = serviceService;
    }

    @GetMapping("/dates/{id}")
    public ResponseEntity<?> getAvailableDates(@PathVariable("id") final Long id) {

        LOGGER.info("[AVAIL-CONTROL] - Reached endpoint for getting available dates for a service. Trying to find service with id: {}", id);
        Optional<Service> serviceOptional = serviceService.findById(id);

        if(serviceOptional.isPresent()) {
            LOGGER.info("[AVAIL-CONTROL] - Successfully found service with id: {}. Fetching available dates...", id);
            List<LocalDate> availableDates = availabilityService.getAvailableDatesForService(serviceOptional.get());
            return new ResponseEntity<>(availableDates, HttpStatus.OK);
        }

        LOGGER.info("[AVAIL-CONTROL] - Unable to find service with id: {}", id);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/times/{id}/{date}")
    public ResponseEntity<?> getAvailableTimes(@PathVariable("id") final Long serviceId,
                                               @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {

        LOGGER.info("[AVAIL-CONTROL] - Reached endpoint for getting available times for a service at a specific date. " +
                "Trying to find service with id {}", serviceId);

        boolean isDateValid = date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusMonths(3));
        if(!isDateValid) {
            LOGGER.info("[AVAIL-CONTROL] - Invalid date");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Service> serviceOptional = serviceService.findById(serviceId);

        if(serviceOptional.isPresent()) {
            LOGGER.info("[AVAIL-CONTROL] - Successfully found service with id: {}. Fetching available times...", serviceId);
            Service service = serviceOptional.get();
            List<LocalTime> availableTimes = availabilityService.getAvailableTimesForServiceAndDate(service, date);
            return new ResponseEntity<>(availableTimes, HttpStatus.OK);
        }
        LOGGER.info("[AVAIL-CONTROL] - Unable to find service with id: {}", serviceId);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
