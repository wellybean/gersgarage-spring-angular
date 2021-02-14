package com.wellybean.gersgarage.service;

import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.model.Vehicle;

public interface VehicleService {
    Vehicle registerVehicle(Vehicle vehicle);
    void deleteVehicle(Vehicle vehicle);
    Optional<Vehicle> getVehicle(Long id);
    Optional<List<Vehicle>> getVehiclesForUser(String username);
}
