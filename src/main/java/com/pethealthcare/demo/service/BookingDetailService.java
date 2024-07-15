package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.response.MostUsedServiceResponse;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.*;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class BookingDetailService {
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServiceRepository serviceRepository;

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

    public MostUsedServiceResponse mostUsedService(int month, int year) {
        List<BookingDetail> bookingDetails = bookingDetailRepository
                .findMostUsedServiceByMonthAndYear(month, year);
        Map<Integer, Integer> serviceIdCount = new HashMap<>();
        for (BookingDetail bookingDetail : bookingDetails) {
            int serviceId = bookingDetail.getServices().getServiceId();
            serviceIdCount.put(serviceId, serviceIdCount.getOrDefault(serviceId, 0) + 1);
        }

        int maxCount = 0;
        for (int count : serviceIdCount.values()) {
            if (count > maxCount) {
                maxCount = count;
            }
        }

        List<Services> mostFrequentServices = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : serviceIdCount.entrySet()) {
            if (entry.getValue() == maxCount) {
                Services service = serviceRepository.findServicesByServiceId(entry.getKey());
                if (service != null) {
                    mostFrequentServices.add(service);
                }
            }
        }

        if (mostFrequentServices.isEmpty()) {
            throw new RuntimeException("No services found");
        }

        return new MostUsedServiceResponse(maxCount, mostFrequentServices);
    }
}
