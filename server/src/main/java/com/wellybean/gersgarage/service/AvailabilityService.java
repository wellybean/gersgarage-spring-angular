package com.wellybean.gersgarage.service;

import com.wellybean.gersgarage.model.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AvailabilityService {
    List<LocalDate> getAvailableDatesForService(Service service);
    List<LocalTime> getAvailableTimesForServiceAndDate(Service service, LocalDate date);
}
