package com.wellybean.gersgarage.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.util.EntityFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AvailabilityServiceImplTest {

    @InjectMocks private AvailabilityServiceImpl availabilityService;
    @Mock private BookingService bookingService;

    @Test public void test_getAvailableDatesForService_succeeds_noBookings() {
        Service service = EntityFactory.getServiceWithNoBookings(60);

        availabilityService.getAvailableDatesForService(service);
        for(LocalDate date = LocalDate.now().plusDays(1); date.isBefore(LocalDate.now().plusMonths(3)); date = date.plusDays(1)) {
            verify(bookingService).getAllBookingsForDate(date);
        }
    }

    @Test public void test_getAvailableDatesForService_succeeds_oneDayWithBookings() {
        Service service = EntityFactory.getServiceWithNoBookings(120);

        // List of already existing bookings for specific date
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(10, 0), new User(), new Vehicle(), service));
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(14, 0), new User(), new Vehicle(), service));

        when(bookingService.getAllBookingsForDate(LocalDate.now().plusDays(7))).thenReturn(Optional.of(bookings));

        availabilityService.getAvailableDatesForService(service);
        for(LocalDate date = LocalDate.now().plusDays(1); date.isBefore(LocalDate.now().plusMonths(3)); date = date.plusDays(1)) {
            if(!date.isEqual(LocalDate.now().plusDays(7))) {
                verify(bookingService).getAllBookingsForDate(date);
            } else {
                verify(bookingService, times(2)).getAllBookingsForDate(date);
            }
        }
    }

    @Test public void test_getAvailableDatesForService_succeeds_oneDayFullyBooked() {
        Service service = EntityFactory.getServiceWithNoBookings(7 * 60);
        LocalDate dateUnderTest = LocalDate.now().plusDays(7);
        // List of already existing bookings for specific date
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(9, 0), new User(), new Vehicle(), service));

        when(bookingService.getAllBookingsForDate(dateUnderTest)).thenReturn(Optional.of(bookings));

        List<LocalDate> availableDates = availabilityService.getAvailableDatesForService(service);
        for(LocalDate date = LocalDate.now().plusDays(1); date.isBefore(LocalDate.now().plusMonths(3)); date = date.plusDays(1)) {
            if(!date.isEqual(dateUnderTest)) {
                verify(bookingService).getAllBookingsForDate(date);
            } else {
                verify(bookingService, times(2)).getAllBookingsForDate(date);
            }
        }
        assertFalse(availableDates.contains(dateUnderTest));
    }

    @Test public void test_getAvailableTimesForServiceAndDate_succeeds_allSlotsAvailable() {
        Service service = EntityFactory.getServiceWithNoBookings(60);
        LocalDate date = LocalDate.now().plusDays(7);
        List<LocalTime> expected = EntityFactory.getListOfAvailableTimes(new int[]{9, 10, 11, 12, 13, 14, 15, 16});

        when(bookingService.getAllBookingsForDate(date)).thenReturn(Optional.empty());

        List<LocalTime> actual = availabilityService.getAvailableTimesForServiceAndDate(service, date);
        assertEquals(expected, actual);
    }

    @Test public void test_getAvailableTimesForServiceAndDate_succeeds_someSlotsAvailable() {
        // Service
        Service service1 = EntityFactory.getServiceWithNoBookings(60);
        Service service2 = EntityFactory.getServiceWithNoBookings(120);
        // Date
        LocalDate date = LocalDate.now().plusDays(7);
        // List of already existing bookings for this date
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(10, 0), new User(), new Vehicle(), service1));
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(14, 0), new User(), new Vehicle(), service2));
        // List of expected available time slots
        List<LocalTime> expected = EntityFactory.getListOfAvailableTimes(new int[]{9, 11, 12, 13, 16});

        when(bookingService.getAllBookingsForDate(date)).thenReturn(Optional.of(bookings));

        List<LocalTime> actual = availabilityService.getAvailableTimesForServiceAndDate(service1, date);
        assertEquals(expected, actual);
    }

    @Test public void test_getAvailableTimesForServiceAndDate_succeeds_someSlotsAvailable_v2() {
        // Service
        Service service1 = EntityFactory.getServiceWithNoBookings(60);
        Service service2 = EntityFactory.getServiceWithNoBookings(120);
        // Date
        LocalDate date = LocalDate.now().plusDays(7);
        // List of already existing bookings for this date
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(10, 0), new User(), new Vehicle(), service1));
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(14, 0), new User(), new Vehicle(), service2));
        // List of expected available time slots
        List<LocalTime> expected = EntityFactory.getListOfAvailableTimes(new int[]{11, 12});

        when(bookingService.getAllBookingsForDate(date)).thenReturn(Optional.of(bookings));

        List<LocalTime> actual = availabilityService.getAvailableTimesForServiceAndDate(service2, date);
        assertEquals(expected, actual);
    }

    @Test public void test_getAvailableTimesForServiceAndDate_succeeds_someSlotsAvailable_v3() {
        // Service
        Service service1 = EntityFactory.getServiceWithNoBookings(60);
        Service service2 = EntityFactory.getServiceWithNoBookings(120);
        // Date
        LocalDate date = LocalDate.now().plusDays(7);
        // List of already existing bookings for this date
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(LocalDate.now(), LocalTime.of(10, 0), new User(), new Vehicle(), service1));
        // List of expected available time slots
        List<LocalTime> expected = EntityFactory.getListOfAvailableTimes(new int[]{11, 12, 13, 14, 15});

        when(bookingService.getAllBookingsForDate(date)).thenReturn(Optional.of(bookings));

        List<LocalTime> actual = availabilityService.getAvailableTimesForServiceAndDate(service2, date);
        assertEquals(expected, actual);
    }
}