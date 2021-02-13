package com.wellybean.gersgarage.service;

import java.util.List;
import java.util.Optional;

import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Vehicle;
import com.wellybean.gersgarage.repository.UserRepository;
import com.wellybean.gersgarage.repository.VehicleRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void deleteVehicle(final Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    @Transactional
    @Override
    public Optional<List<Vehicle>> getVehiclesForUser(final String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()) {
            Optional<List<Vehicle>> vehicles = vehicleRepository.findAllByUser(userOptional.get());
            if(vehicles.isPresent()){
                // This is necessary so that the bookings list is initialised in this transactional context and a LazyInitializationException is avoided
                vehicles.get().forEach(vehicle -> vehicle.getBookings().size());
                return vehicles;
            }
        }
        return Optional.empty();
    }

    @Override
    public Vehicle registerVehicle(final Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> getVehicle(final Long id) {
        return vehicleRepository.findVehicleById(id);
    }
}
