package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.responsitory.*;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    @Autowired
    private  PetRepository petRepository;



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
        List<Booking> bookings = bookingRepository.getBookingByUser(user);
        List<BookingDetail> bookingDetails = new ArrayList<>();
        List<BookingDetail> bookingDetailByBooking;
        for (Booking booking : bookings) {
            bookingDetailByBooking = bookingDetailRepository.findBookingDetailByBooking(booking);
            for (BookingDetail bookingDetail : bookingDetailByBooking) {
                if (bookingDetail.getStatus().equalsIgnoreCase("COMPLETED")) {
                    bookingDetails.add(bookingDetail);
                }
            }


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

    public List<BookingDetail> getBookingDetailByStayCage() {

        List<Pet> pets = petRepository.getPetByStayCage(true);

        List<BookingDetail> bookingDetails = new ArrayList<>();

        for (Pet pet : pets) {
            bookingDetails.addAll(bookingDetailRepository.getBookingDetailByPet(pet));
        }

        return bookingDetails;
    }
}
