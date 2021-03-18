package com.wellybean.gersgarage.controller;

import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.service.ServiceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {

    @InjectMocks
    private ServiceController serviceController;
    @Mock
    private ServiceService serviceService;

    @Test
    public void getAllServices_fails_serviceServiceThrowsException() {
        when(serviceService.getAllServices()).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> serviceController.getAllServices());

        verify(serviceService).getAllServices();
    }

    @Test
    public void getAllServices_succeeds_serviceServiceReturnsEmptyList() {
        when(serviceService.getAllServices()).thenReturn(new ArrayList<>());

        List<Service> result = serviceController.getAllServices();

        assertEquals(result.size(), 0);
        verify(serviceService).getAllServices();
    }

    @Test
    public void getAllServices_succeeds_serviceServiceReturnsNonEmptyList() {
        List<Service> serviceList = new ArrayList<>(
                Arrays.asList(
                        new Service(),
                        new Service(),
                        new Service()));

        when(serviceService.getAllServices()).thenReturn(serviceList);

        List<Service> result = serviceController.getAllServices();

        assertEquals(result, serviceList);
        verify(serviceService).getAllServices();
    }
}