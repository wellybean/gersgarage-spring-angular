package com.wellybean.gersgarage.repository;

import org.springframework.stereotype.Repository;
import com.wellybean.gersgarage.model.Booking;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    
}
