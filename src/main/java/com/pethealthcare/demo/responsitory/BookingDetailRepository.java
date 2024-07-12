package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {

    @Query("SELECT b FROM BookingDetail b WHERE b.date = :date")
    List<BookingDetail> findBookingDetailsFromDate(@Param("date") Date date);
    List<BookingDetail> findBookingDetailByNeedCage(boolean needCage);
    List<BookingDetail> findBookingDetailByUser(User user);
    BookingDetail findBookingDetailByBookingDetailId(int id);
    List<BookingDetail> getBookingDetailsByBooking(Booking booking);


}
