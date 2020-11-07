package com.wellybean.gersgarage.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.*;

@Entity
@Table(
    name = "\"user\"", // user is a reserved word in postgreSQL
    uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email"})
)
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull @NotNull
    @Column(name = "username")
    private String username;

    @NonNull @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NonNull @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NonNull @NotNull
    @Column(name = "email")
    private String email;

    @NonNull @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NonNull @NotNull
    @Column(name = "encoded_password")
    private String encodedPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;
}
