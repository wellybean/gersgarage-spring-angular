package com.wellybean.gersgarage.repository;

import com.wellybean.gersgarage.model.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
}
