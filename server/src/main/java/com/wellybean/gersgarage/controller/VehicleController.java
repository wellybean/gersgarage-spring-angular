package com.wellybean.gersgarage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.wellybean.gersgarage.dto.VehicleDTO;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.security.service.UserDetailsImpl;
import com.wellybean.gersgarage.service.UserService;
import com.wellybean.gersgarage.service.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicle")
@PreAuthorize("hasRole('USER')")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<VehicleDTO> registerVehicle(@Valid @RequestBody Vehicle vehicle) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDetailsImpl) loggedInUser.getPrincipal()).getId();
        Optional<User> userOptional = userService.findById(userId);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            vehicle.setUser(user);
            Vehicle savedVehicle = vehicleService.registerVehicle(vehicle);
            return new ResponseEntity<>(new VehicleDTO(savedVehicle), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<?> getVehiclesForUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        Optional<List<Vehicle>> vehiclesList = vehicleService.getVehiclesForUser(username);

        if(vehiclesList.isPresent()) {
            List<VehicleDTO> response = vehiclesList.get().stream().map(VehicleDTO::new).collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String requestUserUsername = loggedInUser.getName();
        Optional<Vehicle> vehicleOptional = vehicleService.getVehicle(id);
        if(vehicleOptional.isPresent()){
            Vehicle vehicle = vehicleOptional.get();
            if(vehicle.getUser().getUsername().equals(requestUserUsername)){
                vehicleService.deleteVehicle(vehicle);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
