package com.wellybean.gersgarage.controller;

import java.util.List;
import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service")
@PreAuthorize("hasRole('USER')")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public List<Service> getAllServices() {
        return serviceService.getAllServices();
    }
}
