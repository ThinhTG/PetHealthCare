package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    BookingDetail findBookingDetailByBookingDetailId(int id);


    List<BookingDetail> getBookingDetailsByBooking(Booking booking);
}
