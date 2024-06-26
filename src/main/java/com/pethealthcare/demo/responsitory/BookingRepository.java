package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getBookingByUser(User user);

    Booking findBookingByBookingId(int bookingId);
}
