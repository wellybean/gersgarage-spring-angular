package com.wellybean.gersgarage.service;

import java.util.Optional;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for CRUD operations for User entity
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructs instance of UserServiceImpl
     * @param userRepository  user repository interface
     */
    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by its id number
     * @param id  id number
     * @return optional of user
     */
    @Override
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id);
    }
}
