package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findPaymentByBooking_BookingId(int bookingId);
}
