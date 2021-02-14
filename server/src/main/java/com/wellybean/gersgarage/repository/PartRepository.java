package com.wellybean.gersgarage.repository;

import com.wellybean.gersgarage.model.Part;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface PartRepository extends CrudRepository<Part, Long> {
    
}
