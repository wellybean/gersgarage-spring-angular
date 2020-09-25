package com.wellybean.gersgarage.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(
    name = "user",
    uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"})
)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "enconded_password")
    private String encodedPassword;

    @OneToMany(mappedBy = "user")
    private Set<Vehicle> vehicles;

    @OneToMany(mappedBy = "user")
    private Set<Booking> bookings;
}
