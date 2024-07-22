package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Payment;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.*;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.awt.print.Book;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingDetailService {
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;



    public List<BookingDetail> getAllBookingDetail() {
        return bookingDetailRepository.findAll();
    }


    public List<BookingDetail> getAllBookingDetailNeedCage() {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findAll();
        List<BookingDetail> bookingDetailsNeedCage = new ArrayList<BookingDetail>();
        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.isNeedCage()) {
                bookingDetailsNeedCage.add(bookingDetail);
            }

        }
        return bookingDetailsNeedCage;
    }


    public BookingDetail updateNeedCage(int bookingDetailId) {
        Optional<BookingDetail> optionalBookingDetail = bookingDetailRepository.findById(bookingDetailId);
        if (optionalBookingDetail.isPresent()) {
            BookingDetail bookingDetail = optionalBookingDetail.get();
            if (!bookingDetail.isNeedCage()) {
                bookingDetail.setNeedCage(true);
                // Save updated user
            } else {
                bookingDetail.setNeedCage(false);
            }
            bookingDetailRepository.save(bookingDetail);
            return bookingDetail;
        } else {
            return null;
        }
    }

    public List<BookingDetail> getBookingDetailByBookingId(int bookingId) {
        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        return bookingDetailRepository.getBookingDetailsByBooking(booking);
    }

    public List<BookingDetail> getBookingDetailByDate(Date date) {
        return bookingDetailRepository.findBookingDetailsFromDate(date);
    }

    public List<BookingDetail> getBookingDetailByNeedCage() {
        return bookingDetailRepository.findBookingDetailByNeedCage(true);
    }

    public List<BookingDetail> getBookingDetailByUser(int userId) {
        User user = userRepository.findUserByUserId(userId);
        return bookingDetailRepository.getBookingDetailByuser(user);


    }

    public List<BookingDetail> getBookingDetailByCus(int cusId) {
        User user = userRepository.findUserByUserId(cusId);
        List<BookingDetail> bookingDetails = new ArrayList<>(); // Initialize the list
        List<Booking> bookings = bookingRepository.getBookingByUser(user);

        for (Booking booking : bookings) {
            bookingDetails.addAll(bookingDetailRepository.getBookingDetailsByBooking(booking));
        }

        return bookingDetails;
    }

    public List<BookingDetail> getBookingDetailByStatus(String status) {
        return bookingDetailRepository.getBookingDetailByStatus(status);
    }

    public void deleteBookingDetail(int bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        bookingDetail.setStatus("CANCELLED");
        bookingDetailRepository.save(bookingDetail);

        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailsByBooking(bookingDetail.getBooking());

        boolean allCancelled = bookingDetails.stream().allMatch(detail -> detail.getStatus().equalsIgnoreCase("CANCELLED"));


        if (allCancelled) {
            Booking booking = bookingDetail.getBooking();
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
        }
    }

    public BookingDetail updateStatusBookingDetail(int bookingDetailId, String status) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);
        bookingDetail.setStatus(status);
        return bookingDetailRepository.save(bookingDetail);
    }

    public void updateStatusByBookingId(int bookingId, String status) {
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailsByBooking(booking);
        for (BookingDetail detail : bookingDetails) {
            detail.setStatus(status);
        }
        bookingDetailRepository.saveAll(bookingDetails);
    }
}
