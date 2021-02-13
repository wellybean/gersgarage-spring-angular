package com.wellybean.gersgarage.repository;

import com.wellybean.gersgarage.model.User;
import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Booking;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    Optional<List<Booking>> findAllByUser(User user);
    Optional<List<Booking>> findAllByDate(LocalDate date);
}
