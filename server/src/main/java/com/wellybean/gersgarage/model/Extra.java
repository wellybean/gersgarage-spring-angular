package com.wellybean.gersgarage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "extra")
@Data
public class Extra {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price")
    private Double price;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "booking_id")
    private Booking booking;
}