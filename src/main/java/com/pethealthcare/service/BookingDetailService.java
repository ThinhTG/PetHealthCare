package com.pethealthcare.service;

import com.pethealthcare.model.BookingDetail;
import com.pethealthcare.responsitory.BookingDetailRepository;
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
}
