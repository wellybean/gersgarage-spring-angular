package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.repository.ServiceRepository;
import static com.wellybean.gersgarage.util.Constants.VALID_ID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ServiceServiceImplTest {

    @InjectMocks
    private ServiceServiceImpl serviceService;
    @Mock
    private ServiceRepository serviceRepository;

    @Test
    public void test_getAllServices_succeeds() {
        serviceService.getAllServices();
        verify(serviceRepository).findAll();
    }

    @Test
    public void test_findById_succeeds() {
        serviceService.findById(VALID_ID);
        verify(serviceRepository).findById(VALID_ID);
    }
}