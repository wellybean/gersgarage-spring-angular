package com.wellybean.gersgarage.controller;

import com.wellybean.gersgarage.dto.UserDTO;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if(!isAdmin) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<UserDTO> userList = userService.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable Long id) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = loggedInUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        Optional<User> userOptional = userService.findById(id);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if(user.getId().equals(id) || isAdmin) {
                userService.deleteUser(user);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
