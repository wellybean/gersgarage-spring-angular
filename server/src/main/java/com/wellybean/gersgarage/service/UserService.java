package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
}
