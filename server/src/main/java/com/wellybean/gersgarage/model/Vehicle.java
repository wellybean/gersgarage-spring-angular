package com.wellybean.gersgarage.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Entity
@Table(name = "vehicle")
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    @Column(name = "make")
    private String make;

    @NotNull
    @Column(name = "model")
    private String model;

    @NotNull
    @Column(name = "license_plate")
    private String licensePlate;

    @NotNull
    @Column(name = "engine")
    private String engine;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;
}