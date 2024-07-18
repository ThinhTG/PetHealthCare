package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getBookingByUser(User user);
    Booking findBookingByBookingId(int bookingId);
    List<Booking> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Booking> findByStatus(String status);

}
