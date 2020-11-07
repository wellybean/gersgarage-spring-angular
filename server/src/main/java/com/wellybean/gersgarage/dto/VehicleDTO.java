package com.wellybean.gersgarage.dto;

import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.model.Vehicle;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VehicleDTO {
    private Long id;
    private Long userId;
    private String type;
    private String make;
    private String model;
    private String licensePlate;
    private String engine;
    private List<Long> bookingIds;

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.userId = vehicle.getUser().getId();
        this.type = vehicle.getType();
        this.make = vehicle.getMake();
        this.model = vehicle.getModel();
        this.licensePlate = vehicle.getLicensePlate();
        this.engine = vehicle.getEngine();
        this.bookingIds = vehicle.getBookings() == null ? new ArrayList<>() : vehicle.getBookings().stream().map(Booking::getId).collect(Collectors.toList());
    }
}
