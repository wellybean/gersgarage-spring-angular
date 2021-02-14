package com.wellybean.gersgarage.service;

import java.util.Optional;
import com.wellybean.gersgarage.model.User;

public interface UserService {
    Optional<User> findById(Long id);
}
