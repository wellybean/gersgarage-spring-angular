package com.wellybean.gersgarage.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.wellybean.gersgarage.model.User;
import com.wellybean.gersgarage.model.Booking;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    Optional<List<Booking>> findAllByUser(User user);
    Optional<List<Booking>> findAllByDate(LocalDate date);
}
