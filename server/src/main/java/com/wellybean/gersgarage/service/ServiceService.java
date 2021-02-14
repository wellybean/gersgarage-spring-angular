package com.wellybean.gersgarage.service;

import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.model.Service;

public interface ServiceService {
    List<Service> getAllServices();
    Optional<Service> findById(Long id);
}
