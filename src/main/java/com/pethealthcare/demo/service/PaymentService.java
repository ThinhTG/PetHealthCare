package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingCancelRequest;
import com.pethealthcare.demo.dto.request.PaymentCreateRequest;
import com.pethealthcare.demo.mapper.PaymentMapper;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.Payment;
import com.pethealthcare.demo.responsitory.BookingRepository;
import com.pethealthcare.demo.responsitory.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private BookingRepository bookingRepository;

    public Payment createPayment(PaymentCreateRequest request) {
        if (request.getDeposit() < request.getTotal() * 0.3) {
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

    public double getRevenue() {
        double revunue = 0.0;
        for (Payment payment : paymentRepository.findAll()) {
            revunue += payment.getTotal();
        }
        return revunue;
    }

    public double getRevenueInPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findAll();
        double revenue = 0.0;
        for (Payment payment : payments) {
            if (payment.getPaymentDate().isAfter(startDate) && payment.getPaymentDate().isBefore(endDate)) {
                revenue += payment.getTotal();
            }
        }
        return revenue;
    }

    public double returnDeposit(int bookingId, BookingCancelRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Payment payment = paymentRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        LocalDateTime paymentDate = payment.getPaymentDate();
        LocalDateTime sevenDaysAfterPayment = paymentDate.plusDays(7);

            if (request.getPaymentDate().isBefore(sevenDaysAfterPayment)) {
                payment.setDeposit(payment.getDeposit()*0.5);
            }

        return payment.getDeposit();
    }
    }

