package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.demo.mapper.BookingDetailMapper;
import com.pethealthcare.demo.mapper.BookingMapper;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.responsitory.BookingDetailRepository;
import com.pethealthcare.demo.responsitory.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BookingDetailMapper bookingDetailMapper;

    @Transactional
    public void createBooking(BookingCreateRequest request) {
        Booking newBooking = bookingMapper.toBooking(request);
        newBooking.setDate(new Date());
        newBooking = bookingRepository.save(newBooking);

        //List<BookingDetail> detail = new ArrayList<>();
        for (BookingDetailCreateRequest request1 : request.getBookingDetails()) {
            BookingDetail bookingDetail = bookingDetailMapper.toBookingDetail(request1);
            bookingDetail.setBooking(newBooking);

            bookingDetailRepository.save(bookingDetail);
        }
        
    }

}
