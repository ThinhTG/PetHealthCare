package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.dto.request.RevenueResponse;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getBookingByUser(User user);
    Booking findBookingByBookingId(int bookingId);
    List<Booking> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Booking> findByStatus(String status);


    @Query(value = "SELECT new com.pethealthcare.demo.dto.request.RevenueResponse(MONTH(b.payDate), SUM(b.amount))\n" +
            "FROM Payment b \n" +
            "WHERE YEAR(b.payDate) = :year \n" +
            "GROUP BY MONTH(b.payDate)  \n" +
            "ORDER BY MONTH(b.payDate) ASC")
    List<RevenueResponse> getRevenueByMonth(@Param("year") int year);

}
