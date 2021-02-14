package com.wellybean.gersgarage.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.service.AvailabilityService;
import com.wellybean.gersgarage.service.ServiceService;
import com.wellybean.gersgarage.util.EntityFactory;
import static com.wellybean.gersgarage.util.Constants.VALID_ID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AvailabilityControllerTest {

    @InjectMocks
    private AvailabilityController availabilityController;
    @Mock
    private AvailabilityService availabilityService;
    @Mock
    private ServiceService serviceService;

    @Test
    public void test_getAvailableDates_succeeds() {
        Service service = new Service();
        List<LocalDate> availableDates = new ArrayList<>();
        when(serviceService.findById(VALID_ID)).thenReturn(Optional.of(service));
        when(availabilityService.getAvailableDatesForService(service)).thenReturn(availableDates);

        var response = availabilityController.getAvailableDates(VALID_ID);

        assertEquals(new ResponseEntity<>(availableDates, HttpStatus.OK), response);
        verify(serviceService).findById(VALID_ID);
    }

    @Test
    public void test_getAvailableDates_fails_invalidServiceId() {
        when(serviceService.findById(VALID_ID)).thenReturn(Optional.empty());

        var response = availabilityController.getAvailableDates(VALID_ID);

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), response);
        verify(serviceService).findById(VALID_ID);
    }

    @Test
    public void test_getAvailableTimes_succeeds() {
        Service service = EntityFactory.getServiceWithNoBookings(60);
        LocalDate dateUnderTest = LocalDate.now().plusDays(7);
        List<LocalTime> availableTimes = new ArrayList<>();

        when(serviceService.findById(VALID_ID)).thenReturn(Optional.of(service));
        when(availabilityService.getAvailableTimesForServiceAndDate(service, dateUnderTest)).thenReturn(availableTimes);

        var response = availabilityController.getAvailableTimes(VALID_ID, dateUnderTest);
        assertEquals(new ResponseEntity<>(availableTimes, HttpStatus.OK), response);
        verify(serviceService).findById(VALID_ID);
    }

    @Test
    public void test_getAvailableTimes_fails_invalidServiceId() {
        LocalDate date = LocalDate.now().plusDays(7);
        when(serviceService.findById(VALID_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = availabilityController.getAvailableTimes(VALID_ID, date);

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), response);
        verify(serviceService).findById(VALID_ID);
    }

    @Test
    public void test_getAvailableTimes_fails_invalidDate() {
        LocalDate date = LocalDate.now().minusDays(7);

        ResponseEntity<?> response = availabilityController.getAvailableTimes(VALID_ID, date);

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), response);
        verify(serviceService, times(0)).findById(VALID_ID);
    }
}