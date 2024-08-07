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
    private TransactionRepository transactionRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;
    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionService transactionService;

    public List<Refund> getReturn() {
        return refundRepository.findAll();
    }

    public List<Refund> getReturnByCusId(int userId) {
        User user = userRepository.findUserByUserId(userId);
        List<Booking> bookings = bookingRepository.getBookingByUser(user);
        List<Refund> refunds = new ArrayList<>();

        for (Booking booking : bookings) {
            List<BookingDetail> bookingDetails = bookingDetailRepository.findBookingDetailByBooking(booking);
            for (BookingDetail bookingDetail : bookingDetails) {
                Refund refund = refundRepository.findRefundByBookingDetail_BookingDetailId(bookingDetail.getBookingDetailId());
                if (refund != null) {
                    refunds.add(refund);
                }
            }
        }

        return refunds;
    }

    public Refund getReturnByBookingDetailId(int bookingDetailId) {
        return refundRepository.findRefundByBookingDetail_BookingDetailId(bookingDetailId);
    }


    public Refund returnDepositStaffCancelBookingDetail(int bookingDetailId, int userId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        if (bookingDetail == null) {
            throw new EntityNotFoundException("BookingDetail not found with id: " + bookingDetailId);
        }
        Transaction transaction = transactionRepository.findTransactionByBooking_BookingId(bookingDetail.getBooking().getBookingId());
        if (transaction == null) {
            throw new EntityNotFoundException("Transaction not found for BookingId: " + bookingDetail.getBooking().getBookingId());
        }
        Wallet wallet = walletRepository.findWalletByUser_UserId(userId);
        Refund refund = new Refund();
        refund.setBookingDetail(bookingDetail);
        double priceAfterCancel = bookingDetail.getServices().getPrice();
        priceAfterCancel *= 1;
        refund.setRefundPercent(100);
        refund.setAmount((int) priceAfterCancel);
        refund.setRefundDate(LocalDate.now());
        transactionService.refund(wallet.getWalletId(), bookingDetail.getBooking().getBookingId(), (int) priceAfterCancel);

        return refundRepository.save(refund);
    }


    public Refund returnDepositCancelBookingDetail(int bookingDetailId, int userId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        if (bookingDetail == null) {
            throw new EntityNotFoundException("BookingDetail not found with id: " + bookingDetailId);
        }
        Transaction transaction = transactionRepository.findTransactionByBooking_BookingId(bookingDetail.getBooking().getBookingId());
        if (transaction == null) {
            throw new EntityNotFoundException("Transaction not found for BookingId: " + bookingDetail.getBooking().getBookingId());
        }
        Wallet wallet = walletRepository.findWalletByUser_UserId(userId);
        Refund refund = new Refund();
        refund.setBookingDetail(bookingDetail);
        LocalDate bookingDetailDate = bookingDetail.getDate();
        LocalDate sixDaysAfterPayment = bookingDetailDate.minusDays(6);
        LocalDate threeDaysAfterPayment = bookingDetailDate.minusDays(3);
        double priceAfterCancel = bookingDetail.getServices().getPrice();
        if ((LocalDate.now().isAfter(sixDaysAfterPayment) || LocalDate.now().isEqual(sixDaysAfterPayment)) && (LocalDate.now().isBefore(threeDaysAfterPayment) || LocalDate.now().isEqual(threeDaysAfterPayment))) {
            priceAfterCancel = priceAfterCancel * 0.75;
            refund.setRefundPercent(75);
            refund.setAmount((int) priceAfterCancel);
            refund.setRefundDate(LocalDate.now());
            transactionService.refund(wallet.getWalletId(), bookingDetail.getBooking().getBookingId(), (int) priceAfterCancel);
        } else if (LocalDate.now().isAfter(threeDaysAfterPayment)) {
            refund.setAmount(0);
            refund.setRefundDate(LocalDate.now());
            refund.setRefundPercent(0);
        } else if (LocalDate.now().isBefore(sixDaysAfterPayment)) {
            refund.setRefundDate(LocalDate.now());
            refund.setAmount(transaction.getAmount());
            refund.setRefundPercent(100);
            transactionService.refund(wallet.getWalletId(), bookingDetail.getBooking().getBookingId(), (int) priceAfterCancel);
        }
        return refundRepository.save(refund);
    }
}



