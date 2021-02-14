package com.wellybean.gersgarage.repository;

import com.wellybean.gersgarage.model.Extra;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ExtraRepository extends CrudRepository<Extra, Long> {
}