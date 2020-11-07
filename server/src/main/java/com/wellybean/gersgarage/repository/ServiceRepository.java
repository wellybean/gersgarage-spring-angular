package com.wellybean.gersgarage.repository;

import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Service;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {
}
