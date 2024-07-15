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
    @Query(value = "SELECT new com.pethealthcare.demo.dto.request.RevenueResponse(MONTH(b.date), SUM(b.totalPrice) AS AMOUNT)\n" +
            "FROM Booking b \n" +
            "WHERE YEAR(b.date) = :year \n" +
            "GROUP BY MONTH(b.date)  \n" +
            "ORDER BY MONTH(b.date) ASC")
    List<RevenueResponse> getRevenueByMonth(@Param("year") int year);
}
