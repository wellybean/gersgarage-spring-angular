package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
