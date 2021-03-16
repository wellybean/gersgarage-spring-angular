package com.wellybean.gersgarage.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import com.wellybean.gersgarage.dto.VehicleDTO;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.security.service.UserDetailsImpl;
import com.wellybean.gersgarage.service.UserService;
import com.wellybean.gersgarage.service.VehicleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Vehicle Controller
 */
@RestController
@RequestMapping("/api/vehicle")
@PreAuthorize("hasRole('USER')")
public class VehicleController {

    private static final Logger LOGGER = LogManager.getLogger(VehicleController.class);

    private final VehicleService vehicleService;
    private final UserService userService;

    /**
     * Constructs instance of vehicle controller
     * @param vehicleService  vehicle service
     * @param userService  user service
     */
    @Autowired
    public VehicleController(VehicleService vehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    /**
     * Exposes endpoint for registering a new vehicle
     * @param vehicle  new vehicle required information
     * @return persisted vehicle
     */
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

    /**
     * Exposes endpoint for obtaining all vehicles registered for a particular user
     * @return list of registered vehicles
     */
    @GetMapping
    public ResponseEntity<?> getVehiclesForUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();

        LOGGER.info("[VEHICLE-CONTROL] - Going to fetch vehicles for user {}...", username);
        Optional<List<Vehicle>> vehiclesList = vehicleService.getVehiclesForUser(username);

        if(vehiclesList.isPresent()) {
            LOGGER.info("[VEHICLE-CONTROL] - Found vehicles for user {}.", username);
            List<VehicleDTO> response = vehiclesList.get().stream().map(VehicleDTO::new).collect(Collectors.toList());
            LOGGER.info("[VEHICLE-CONTROL] - Returning list of {} vehicles for user {}", response.size(), username);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Exposes endpoint for deleting a registered vehicle for a particular user
     * @param id  vehicle id
     * @return HTTP response with 204 is deleted successfully and 400 if invalid request
     */
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
