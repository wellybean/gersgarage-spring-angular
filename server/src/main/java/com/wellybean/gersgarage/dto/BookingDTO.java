package com.wellybean.gersgarage.dto;

import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.model.Service;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
public class BookingDTO {

    private Long id;
    private String date;
    private String time;
    private Long userId;
    private VehicleDTO vehicle;
    private Service service;
    private String status;
    private String comments;

    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.date = booking.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = booking.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.userId = booking.getUser().getId();
        this.vehicle = new VehicleDTO(booking.getVehicle());
        this.service = booking.getService();
        this.status = booking.getStatus();
        this.comments = booking.getComments();
    }
}
