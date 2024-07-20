package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer>{
    Refund findRefundByBooking_BookingId(int bookingId);

}
