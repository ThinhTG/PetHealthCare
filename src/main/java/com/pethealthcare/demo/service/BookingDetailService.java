package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.responsitory.BookingDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingDetailService {
    @Autowired
    private BookingDetailRepository bookingDetailRepository;


    public List<BookingDetail> getAllBookingDetail() {
        return bookingDetailRepository.findAll();
    }

    public List<BookingDetail> getBookingDetailByNeedCage() {
        return bookingDetailRepository.findBookingDetailByNeedCage(true);
    }
}
