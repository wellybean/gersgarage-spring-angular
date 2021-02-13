package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<Service> getAllServices();
    Optional<Service> findById(Long id);
}
