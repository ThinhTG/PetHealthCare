package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Payment;
import com.pethealthcare.demo.model.Refund;
import com.pethealthcare.demo.responsitory.BookingDetailRepository;
import com.pethealthcare.demo.responsitory.BookingRepository;
import com.pethealthcare.demo.responsitory.PaymentRepository;
import com.pethealthcare.demo.responsitory.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RefundService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private RefundRepository refundRepository;

    public Refund returnDepositCancelBooking(int bookingId) {
        Refund refund = refundRepository.findRefundByBooking_BookingId(bookingId);
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        Payment payment = paymentRepository.findPaymentByBooking_BookingId(bookingId);
        LocalDate bookingDate = booking.getDate();
        LocalDate sixDaysAfterBooking = bookingDate.plusDays(6);
        LocalDate threeDaysAfterBooking = bookingDate.plusDays(3);

        if ((LocalDate.now().isAfter(threeDaysAfterBooking) || LocalDate.now().isEqual(threeDaysAfterBooking))
                && (LocalDate.now().isBefore(sixDaysAfterBooking) || LocalDate.now().isEqual(sixDaysAfterBooking))) {
            refund.setPayDate(LocalDate.now());
            int newAmount = (int) (payment.getAmount() - payment.getAmount() * 0.25);
            refund.setAmount(newAmount);
            refund.setRefundPercent(25);

        } else if (LocalDate.now().isAfter(sixDaysAfterBooking)) {
            refund.setPayDate(LocalDate.now());
            refund.setAmount(0);
            refund.setRefundPercent(0);
        } else if (LocalDate.now().isBefore(threeDaysAfterBooking)) {
            refund.setPayDate(LocalDate.now());
            refund.setAmount(payment.getAmount());
            refund.setRefundPercent(100);
        }

        return refundRepository.save(refund);
    }



    public Refund returnDepositCacnelBookingDetail(int bookingId, int bookingDetailId) {
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        Payment payment = paymentRepository.findPaymentByBooking_BookingId(bookingId);
        Refund refund = refundRepository.findRefundByBooking_BookingId(bookingId);
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        LocalDate bookingDetailDate = bookingDetail.getDate();
        LocalDate sixDaysAfterPayment = bookingDetailDate.plusDays(6);
        LocalDate threeDaysAfterPayment = bookingDetailDate.plusDays(3);
        int newAmount = 0;
        if ( ( LocalDate.now().isAfter(threeDaysAfterPayment) || LocalDate.now().isEqual(threeDaysAfterPayment)) && (LocalDate.now().isBefore(sixDaysAfterPayment) || LocalDate.now().isEqual(sixDaysAfterPayment))) {
            double priceAfterCancel = bookingDetail.getServices().getPrice() * 0.25;
            newAmount = payment.getAmount() - (int)priceAfterCancel;
            refund.setRefundPercent(75);
            refund.setAmount(newAmount);
            refund.setPayDate(LocalDate.now());
        } else if (LocalDate.now().isAfter(sixDaysAfterPayment)){
            refund.setAmount(0);
            refund.setPayDate(LocalDate.now());
            refund.setRefundPercent(0);
        } else if (LocalDate.now().isBefore(threeDaysAfterPayment)) {
            refund.setPayDate(LocalDate.now());
            refund.setAmount(payment.getAmount());
            refund.setRefundPercent(100);
        }
        return refundRepository.save(refund);
    }

}
