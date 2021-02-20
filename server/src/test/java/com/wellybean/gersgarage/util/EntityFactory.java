package com.wellybean.gersgarage.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.wellybean.gersgarage.model.ERole;
import com.wellybean.gersgarage.model.Role;
import com.wellybean.gersgarage.model.Service;
import com.wellybean.gersgarage.model.User;
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

    public static User getUser(List<Role> roles) {
        User user = new User();
        user.setId(VALID_ID);
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
}
