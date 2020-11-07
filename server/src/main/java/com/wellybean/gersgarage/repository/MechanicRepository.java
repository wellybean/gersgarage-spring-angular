package com.wellybean.gersgarage.repository;

import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Mechanic;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface MechanicRepository extends CrudRepository<Mechanic, Long> {
    
}