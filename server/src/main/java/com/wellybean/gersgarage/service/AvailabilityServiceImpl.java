package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private BookingService bookingService;

    @Override
    public List<LocalDate> getAvailableDatesForService(com.wellybean.gersgarage.model.Service service) {
        // Final list
        List<LocalDate> listAvailableDates = new ArrayList<>();

        // Variables for loop
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate threeMonthsFromTomorrow = tomorrow.plusMonths(3);

        // Loop to check for available dates in next three months
        for(LocalDate date = tomorrow; date.isBefore(threeMonthsFromTomorrow); date = date.plusDays(1)) {
            // Pulls bookings for specific date
            Optional<List<Booking>> optionalBookingList = bookingService.getAllBookingsForDate(date);
            /* If there are bookings for that date, check for time availability for service,
             if not booking then date available */
            if(optionalBookingList.isPresent()) {
                if(getAvailableTimesForServiceAndDate(service, date).size() > 0) {
                    listAvailableDates.add(date);
                }
            } else {
                listAvailableDates.add(date);
            }
        }
        return listAvailableDates;
    }

    @Override
    public List<LocalTime> getAvailableTimesForServiceAndDate(com.wellybean.gersgarage.model.Service service, LocalDate date) {

        List<LocalTime> listAvailableTimes = new ArrayList<>();

        LocalTime garageOpening = LocalTime.of(9,0);
        LocalTime garageClosing = LocalTime.of(17, 0);

        Optional<List<Booking>> optionalBookingList = bookingService.getAllBookingsForDate(date);

        if(optionalBookingList.isPresent()) {
            List<Booking> bookingsList = optionalBookingList.get();
            for(LocalTime slot = garageOpening; slot.isBefore(garageClosing); slot = slot.plusHours(1)) {
                int serviceDurationInHours = service.getDurationInMinutes() / 60;
                boolean isSlotAvailable = true;
                for(LocalTime time = garageOpening; time.isBefore(time.plusHours(serviceDurationInHours)); time = time.plusHours(1)) {
                    for(Booking booking: bookingsList) {
                        if (booking.getTime().equals(time)) {
                            isSlotAvailable = false;
                            break;
                        }
                    }
                }
                if(isSlotAvailable) {
                    listAvailableTimes.add(slot);
                }
                slot.plusHours(serviceDurationInHours - 1);
            }
        } else {
            for(LocalTime slot = garageOpening; slot.isBefore(garageClosing.minusMinutes(service.getDurationInMinutes()).plusHours(1)); slot = slot.plusHours(1)) {
                listAvailableTimes.add(slot);
            }
        }

        return listAvailableTimes;
    }
}
