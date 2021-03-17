package com.wellybean.gersgarage.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.wellybean.gersgarage.model.*;

import static com.wellybean.gersgarage.model.ERole.ROLE_USER;
import static com.wellybean.gersgarage.util.Constants.*;

public class EntityFactory {
    public static List<LocalTime> getListOfAvailableTimes(int[] availableHours) {
        return Arrays.stream(availableHours).mapToObj(hour -> LocalTime.of(hour, 0)).collect(Collectors.toList());
    }

    public static Service getServiceWithNoBookings(int duration) {
        Service service = new Service();

        service.setId(VALID_ID);
        service.setDescription(SERVICE_DESCRIPTION);
        service.setDurationInMinutes(duration);
        service.setPrice(SERVICE_PRICE);
        service.setBookings(new ArrayList<>());

        return service;
    }

    public static User getRegularUser() {
        Role role = new Role();
        role.setName(ROLE_USER);
        return getUser(Collections.singletonList(role));
    }

    public static User getUser(List<Role> roles) {
        return getUser(VALID_ID, roles);
    }

    public static User getUser(Long id, List<Role> roles) {
        User user = new User();
        user.setId(VALID_ID);
        user.setUsername(VALID_USERNAME);
        user.setEmail(VALID_EMAIL);
        user.setFirstName(VALID_FIRST_NAME);
        user.setLastName(VALID_LAST_NAME);
        user.setRoles(roles);
        return user;
    }

    public static List<Role> listWithAllRoles() {
        List<Role> roles = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName(ERole.ROLE_USER);

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName(ERole.ROLE_MECHANIC);

        Role role3 = new Role();
        role3.setId(3L);
        role3.setName(ERole.ROLE_ADMIN);

        roles.add(role1);
        roles.add(role2);
        roles.add(role3);

        return roles;
    }

    public static Vehicle getVehicle() {
        return getVehicle(VALID_ID, getRegularUser());
    }

    public static List<Vehicle> getVehicleListForUser(int amount) {
        List<Vehicle> list = new ArrayList<>();
        for(int i = 1; i <= amount; i++) {
            list.add(getVehicle((long) i, getRegularUser()));
        }
        return list;
    }

    public static Vehicle getVehicle(Long id, User user) {
        Vehicle vehicle = new Vehicle();
        vehicle.setUser(user);
        vehicle.setId(id);
        vehicle.setEngine(VALID_ENGINE);
        vehicle.setMake(VALID_MAKE);
        vehicle.setModel(VALID_MODEL);
        vehicle.setLicensePlate(VALID_LICENSE_PLATE);
        vehicle.setType(VALID_TYPE);
        return vehicle;
    }
}
