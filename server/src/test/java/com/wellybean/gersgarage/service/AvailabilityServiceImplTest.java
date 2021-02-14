package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.util.EntityFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AvailabilityServiceImplTest {

    @InjectMocks private AvailabilityServiceImpl availabilityService;
    @Mock private BookingService bookingService;

    @Test
    public void test_getAvailableTimesForServiceAndDate_succeeds_allSlotsAvailable() {
        Service service = new Service(1L, "Service", 100.00, 60, null);
        LocalDate date = LocalDate.now().plusDays(7);
        List<LocalTime> expected = EntityFactory.getListOfAvailableTimes(new int[]{9, 10, 11, 12, 13, 14, 15, 16});

        when(bookingService.getAllBookingsForDate(date)).thenReturn(Optional.empty());

        List<LocalTime> actual = availabilityService.getAvailableTimesForServiceAndDate(service, date);
        assertEquals(expected, actual);
    }

    @Test
    public void test_getAvailableTimesForServiceAndDate_succeeds_someSlotsAvailable() {
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

    @Test
    public void test_getAvailableTimesForServiceAndDate_succeeds_someSlotsAvailable_v2() {
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

    @Test
    public void test_getAvailableTimesForServiceAndDate_succeeds_someSlotsAvailable_v3() {
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