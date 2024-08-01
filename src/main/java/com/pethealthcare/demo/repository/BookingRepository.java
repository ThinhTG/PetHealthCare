package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.enums.BookingStatus;
import com.pethealthcare.demo.response.RevenueResponse;
import com.pethealthcare.demo.model.Booking;
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

    @Query(value = "SELECT new com.pethealthcare.demo.response.RevenueResponse(MONTH(b.transactionDate), (SUM(b.amount) - COALESCE(SUM(r.amount * r.refundPercent / 100), 0))) " +
            "FROM Transaction b " +
            "LEFT JOIN Refund r ON b.transactionId = r.transactionNo " +
            "WHERE YEAR(b.transactionDate) = :year " +
            "GROUP BY MONTH(b.transactionDate) " +
            "ORDER BY MONTH(b.transactionDate) ASC")
    List<RevenueResponse> getRevenueByMonth(@Param("year") int year);

    List<Booking> findBookingByUser_Phone(String phone);

    Booking findBookingByUser_UserIdAndStatusIn(int userId, List<BookingStatus> status);

    List<Booking> findBookingByUser(User user);
}
