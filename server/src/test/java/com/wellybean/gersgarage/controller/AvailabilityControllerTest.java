package com.wellybean.gersgarage.controller;

import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.service.AvailabilityService;
import com.wellybean.gersgarage.service.ServiceService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AvailabilityControllerTest {

    private static final long SERVICE_ID = 1L;

    @InjectMocks private AvailabilityController availabilityController;
    @Mock private AvailabilityService availabilityService;
    @Mock private ServiceService serviceService;

    @Test
    public void test_getAvailableDates_succeeds() {
        Service service = new Service();
        List<LocalDate> availableDates = new ArrayList<>();
        when(serviceService.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        when(availabilityService.getAvailableDatesForService(service)).thenReturn(availableDates);

        var response = availabilityController.getAvailableDates(SERVICE_ID);

        assertEquals(new ResponseEntity<>(availableDates, HttpStatus.OK), response);
        verify(serviceService).findById(SERVICE_ID);
    }

    @Test
    public void test_getAvailableDates_fails_invalidServiceId() {
        when(serviceService.findById(SERVICE_ID)).thenReturn(Optional.empty());

        var response = availabilityController.getAvailableDates(SERVICE_ID);

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), response);
        verify(serviceService).findById(SERVICE_ID);
    }

    @Test
    public void test_getAvailableTimes_succeeds() {
        Service service = new Service();
        List<LocalDate> availableDates = new ArrayList<>();
        when(serviceService.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        when(availabilityService.getAvailableDatesForService(service)).thenReturn(availableDates);

        var response = availabilityController.getAvailableDates(SERVICE_ID);

        assertEquals(new ResponseEntity<>(availableDates, HttpStatus.OK), response);
        verify(serviceService).findById(SERVICE_ID);
    }

    @Test
    public void test_getAvailableTimes_fails_invalidServiceId() {
        LocalDate date = LocalDate.now().plusDays(7);
        when(serviceService.findById(SERVICE_ID)).thenReturn(Optional.empty());

        ResponseEntity<?> response = availabilityController.getAvailableTimes(SERVICE_ID, date);

        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), response);
        verify(serviceService).findById(SERVICE_ID);
    }
}