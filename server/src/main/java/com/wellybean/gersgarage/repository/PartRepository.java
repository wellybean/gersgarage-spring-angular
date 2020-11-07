package com.wellybean.gersgarage.repository;

import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Part;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface PartRepository extends CrudRepository<Part, Long> {
    
}
