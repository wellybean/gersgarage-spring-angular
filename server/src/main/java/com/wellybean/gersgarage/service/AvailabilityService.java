package com.wellybean.gersgarage.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.wellybean.gersgarage.model.Service;

public interface AvailabilityService {
    List<LocalDate> getAvailableDatesForService(Service service);
    List<LocalTime> getAvailableTimesForServiceAndDate(Service service, LocalDate date);
}
