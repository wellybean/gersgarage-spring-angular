package com.wellybean.gersgarage.repository;

import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    Optional<List<Vehicle>> findAllByUser(User user);
    Optional<Vehicle> findVehicleById(Long id);
    void delete(Vehicle vehicle);
}
