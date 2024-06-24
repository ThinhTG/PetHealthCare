package com.pethealthcare.service;

import com.pethealthcare.dto.request.PaymentCreateRequest;
import com.pethealthcare.mapper.PaymentMapper;
import com.pethealthcare.model.Booking;
import com.pethealthcare.model.Payment;
import com.pethealthcare.responsitory.BookingRepository;
import com.pethealthcare.responsitory.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private BookingRepository bookingRepository;

    public Payment createPayment(PaymentCreateRequest request) {
        if (request.getDeposit() < request.getTotal()*0.3) {
            throw new IllegalArgumentException("Deposit must be at least 30% of total");
        }
        Payment payment = paymentMapper.toPayment(request);
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (request.getDeposit() == request.getTotal()) {
            booking.setStatus("PAID");
        } else {
            booking.setStatus("DEPOSITED");
        }

        bookingRepository.save(booking);

        payment.setBooking(booking);
        return paymentRepository.save(payment);
    }
}
