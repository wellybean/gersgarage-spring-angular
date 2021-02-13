package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService{

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<com.wellybean.gersgarage.model.Service> getAllServices() {
        List<com.wellybean.gersgarage.model.Service> listOfServices = new ArrayList<>();
        serviceRepository.findAll().forEach(listOfServices::add);
        return listOfServices;
    }

    @Override
    public Optional<com.wellybean.gersgarage.model.Service> findById(Long id) {
        return serviceRepository.findById(id);
    }
}
