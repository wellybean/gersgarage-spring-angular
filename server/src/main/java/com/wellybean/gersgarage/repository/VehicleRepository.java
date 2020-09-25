package com.wellybean.gersgarage.repository;

import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    
}
