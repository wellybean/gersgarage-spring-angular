package com.wellybean.gersgarage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for CRUD operations for Service entity
 */
@Service
public class ServiceServiceImpl implements ServiceService{

    private final ServiceRepository serviceRepository;

    /**
     * Constructs instance of ServiceServiceImpl
     * @param serviceRepository  service repository interface
     */
    @Autowired
    public ServiceServiceImpl(final ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Fetches all available services
     * @return list of all available services
     */
    @Override
    public List<com.wellybean.gersgarage.model.Service> getAllServices() {
        List<com.wellybean.gersgarage.model.Service> listOfServices = new ArrayList<>();
        serviceRepository.findAll().forEach(listOfServices::add);
        return listOfServices;
    }

    /**
     * Finds a service by id number
     * @param id  id number
     * @return optional of service
     */
    @Override
    public Optional<com.wellybean.gersgarage.model.Service> findById(Long id) {
        return serviceRepository.findById(id);
    }
}
