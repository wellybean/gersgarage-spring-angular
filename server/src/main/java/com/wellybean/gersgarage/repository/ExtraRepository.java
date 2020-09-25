package com.wellybean.gersgarage.repository;

import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Extra;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ExtraRepository extends CrudRepository<Extra, Long> {
    
}