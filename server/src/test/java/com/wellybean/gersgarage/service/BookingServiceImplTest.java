package com.wellybean.gersgarage.service;

import java.time.LocalDate;
import com.wellybean.gersgarage.model.Booking;
import com.wellybean.gersgarage.repository.BookingRepository;
import com.wellybean.gersgarage.repository.UserRepository;
import static com.wellybean.gersgarage.util.Constants.VALID_ID;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;

    @Ignore
    @Test
    public void getAllBookingsForUser() {
    }

    @Test
    public void test_getAllBookingsForDate_succeeds() {
        LocalDate date = LocalDate.now().plusDays(7);
        bookingService.getAllBookingsForDate(date);
        verify(bookingRepository).findAllByDate(date);
    }

    @Test
    public void test_makeBooking_succeeds() {
        Booking booking = new Booking();
        bookingService.makeBooking(booking);
        verify(bookingRepository).save(booking);
    }

    @Test
    public void test_deleteBooking_succeeds() {
        Booking booking = new Booking();
        bookingService.deleteBooking(booking);
        verify(bookingRepository).delete(booking);
    }

    @Test
    public void test_getBooking_succeeds() {
        bookingService.getBooking(VALID_ID);
        verify(bookingRepository).findById(VALID_ID);
    }
}