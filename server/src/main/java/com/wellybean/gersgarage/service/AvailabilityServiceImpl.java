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

    private final BookingService bookingService;

    @Autowired
    public AvailabilityServiceImpl(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public List<LocalDate> getAvailableDatesForService(final com.wellybean.gersgarage.model.Service service) {
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

    /**
     * Computes which time slots are available for a specific service on a specific date
     * @param service  the maintenance service
     * @param date  the date of the maintenance service
     * @return  the list of available slots
     */
    @Override
    public List<LocalTime> getAvailableTimesForServiceAndDate(final com.wellybean.gersgarage.model.Service service, LocalDate date) {

        LocalTime garageOpening = LocalTime.of(9,0);
        LocalTime garageClosing = LocalTime.of(17, 0);

        // List of all slots set as available
        List<LocalTime> listAvailableTimes = new ArrayList<>();
        for(LocalTime slot = garageOpening; slot.isBefore(garageClosing); slot = slot.plusHours(1)) {
            listAvailableTimes.add(slot);
        }

        Optional<List<Booking>> optionalBookingList = bookingService.getAllBookingsForDate(date);

        // This removes slots that are occupied by bookings on the same day
        if(optionalBookingList.isPresent()) {
            List<Booking> bookingsList = optionalBookingList.get();
            for(Booking booking : bookingsList) {
                int bookingDurationInHours = booking.getService().getDurationInMinutes() / 60;
                // Loops through all slots used by the booking and removes them from list of available slots
                for(LocalTime bookingTime = booking.getTime();
                    bookingTime.isBefore(booking.getTime().plusHours(bookingDurationInHours));
                    bookingTime = bookingTime.plusHours(1)) {

                    listAvailableTimes.remove(bookingTime);
                }
            }
        }

        int serviceDurationInHours = service.getDurationInMinutes() / 60;

        // To avoid concurrent modification of list
        List<LocalTime> listAvailableTimesCopy = new ArrayList<>(listAvailableTimes);

        // This removes slots that are available but that are not enough to finish the service. Example: 09:00 is
        // available but 10:00 is not. For a service that takes two hours, the 09:00 slot should be removed.
        for(LocalTime slot : listAvailableTimesCopy) {
            boolean isSlotAvailable = true;
            // Loops through the next slots within the duration of the service
            for(LocalTime nextSlot = slot.plusHours(1); nextSlot.isBefore(slot.plusHours(serviceDurationInHours)); nextSlot = nextSlot.plusHours(1)) {
                if(!listAvailableTimes.contains(nextSlot)) {
                    isSlotAvailable = false;
                    break;
                }
            }
            if(!isSlotAvailable) {
                listAvailableTimes.remove(slot);
            }
        }

        return listAvailableTimes;
    }
}
