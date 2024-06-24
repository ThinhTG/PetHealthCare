package com.pethealthcare.responsitory;

import com.pethealthcare.model.Booking;
import com.pethealthcare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getBookingByUser(User user);
}
