package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Integer>{
    Refund findRefundByBookingDetail_BookingDetailId(int bookingDetailId);



}
