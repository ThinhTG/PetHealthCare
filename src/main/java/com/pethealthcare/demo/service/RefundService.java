package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UserRepository userRepository;

//    public Refund returnDepositCancelBooking(int bookingId) {
//        Refund refund = refundRepository.findRefundByBooking_BookingId(bookingId);
//        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
//        Payment payment = paymentRepository.findPaymentByBooking_BookingId(bookingId);
//        LocalDate bookingDate = booking.getDate();
//        LocalDate sixDaysAfterBooking = bookingDate.plusDays(6);
//        LocalDate threeDaysAfterBooking = bookingDate.plusDays(3);
//
//        if ((LocalDate.now().isAfter(threeDaysAfterBooking) || LocalDate.now().isEqual(threeDaysAfterBooking))
//                && (LocalDate.now().isBefore(sixDaysAfterBooking) || LocalDate.now().isEqual(sixDaysAfterBooking))) {
//            refund.setPayDate(LocalDate.now());
//            int newAmount = (int) (payment.getAmount() - payment.getAmount() * 0.25);
//            refund.setAmount(newAmount);
//            refund.setRefundPercent(25);
//
//        } else if (LocalDate.now().isAfter(sixDaysAfterBooking)) {
//            refund.setPayDate(LocalDate.now());
//            refund.setAmount(0);
//            refund.setRefundPercent(0);
//        } else if (LocalDate.now().isBefore(threeDaysAfterBooking)) {
//            refund.setPayDate(LocalDate.now());
//            refund.setAmount(payment.getAmount());
//            refund.setRefundPercent(100);
//        }
//
//        return refundRepository.save(refund);
//    }

    public List<Refund> getReturn(){
        return refundRepository.findAll();
    }

    public List<Refund> getReturnByCusId(int userId) {
        User user = userRepository.findUserByUserId(userId);
        List<Booking> bookings = bookingRepository.getBookingByUser(user);
        List<BookingDetail> bookingDetails = new ArrayList<>();
        List<BookingDetail> bookingDetailByBooking;
        List<Refund> refunds = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDetailByBooking = bookingDetailRepository.findBookingDetailByBooking(booking);
            for (BookingDetail bookingDetail : bookingDetailByBooking) {
                    bookingDetails.add(bookingDetail);

            }
            for (Refund refund : refundRepository.findAll()) {
                for (BookingDetail bookingDetail : bookingDetails) {
                    if (refund.getBookingDetail().getBookingDetailId() == bookingDetail.getBookingDetailId()) {
                        refunds.add(refund);
                    }
                }
            }
        }
        return refunds;
    }

    public Refund getReturnByBookingDetailId(int bookingDetailId) {
        return refundRepository.findRefundByBookingDetail_BookingDetailId(bookingDetailId);
    }



    public Refund returnDepositCancelBookingDetail(int bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        if (bookingDetail == null) {
            throw new EntityNotFoundException("BookingDetail not found with id: " + bookingDetailId);
        }
        Payment payment = paymentRepository.findPaymentByBooking_BookingId(bookingDetail.getBooking().getBookingId());
        if (payment == null) {
            throw new EntityNotFoundException("Payment not found for BookingId: " + bookingDetail.getBooking().getBookingId());
        }
        Refund refund = new Refund();
        refund.setBookingDetail(bookingDetail);
        LocalDate bookingDetailDate = bookingDetail.getDate();
        LocalDate sixDaysAfterPayment = bookingDetailDate.minusDays(6);
        LocalDate threeDaysAfterPayment = bookingDetailDate.minusDays(3);
        if ((LocalDate.now().isAfter(sixDaysAfterPayment) || LocalDate.now().isEqual(sixDaysAfterPayment)) && (LocalDate.now().isBefore(threeDaysAfterPayment) || LocalDate.now().isEqual(threeDaysAfterPayment))) {
            double priceAfterCancel = bookingDetail.getServices().getPrice() * 0.75;
            refund.setRefundPercent(75);
            refund.setAmount((int) priceAfterCancel);
            refund.setRefundDate(LocalDate.now());
        } else if (LocalDate.now().isAfter(threeDaysAfterPayment)) {
            refund.setAmount(0);
            refund.setRefundDate(LocalDate.now());
            refund.setRefundPercent(0);
        } else if (LocalDate.now().isBefore(sixDaysAfterPayment)) {
            refund.setRefundDate(LocalDate.now());
            refund.setAmount(payment.getAmount());
            refund.setRefundPercent(100);
        }
        return refundRepository.save(refund);
    }
}



