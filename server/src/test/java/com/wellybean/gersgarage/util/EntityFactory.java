package com.wellybean.gersgarage.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.wellybean.gersgarage.model.Service;
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
}
