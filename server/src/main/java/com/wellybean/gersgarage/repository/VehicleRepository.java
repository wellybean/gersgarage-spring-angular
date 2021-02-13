package com.wellybean.gersgarage.repository;

import com.wellybean.gersgarage.model.User;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    Optional<List<Vehicle>> findAllByUser(User user);
    Optional<Vehicle> findVehicleById(Long id);
    void delete(Vehicle vehicle);
}
