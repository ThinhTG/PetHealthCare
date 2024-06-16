package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.demo.mapper.BookingDetailMapper;
import com.pethealthcare.demo.mapper.BookingMapper;
import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.responsitory.BookingDetailRepository;
import com.pethealthcare.demo.responsitory.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Autowired
    private ServiceSlotService serviceSlotService;

    @Transactional
    public void createBooking(BookingCreateRequest request) {
        Booking newBooking = bookingMapper.toBooking(request);
        newBooking.setDate(new Date());
        newBooking = bookingRepository.save(newBooking);

        for (BookingDetailCreateRequest request1 : request.getBookingDetails()) {
            BookingDetail bookingDetail = bookingDetailMapper.toBookingDetail(request1);
            bookingDetail.setBooking(newBooking);

            User user = new User();
            user.setUserId(request1.getVeterinarianId());
            bookingDetail.setUser(user);

            Pet pet = new Pet();
            pet.setPetId(request1.getPetId());
            bookingDetail.setPet(pet);

            Services services = new Services();
            services.setServiceId(request1.getServiceId());
            bookingDetail.setServices(services);

            serviceSlotService.bookedSlot(request1.getVeterinarianId(), request1.getDate(), request1.getSlotId());
            bookingDetailRepository.save(bookingDetail);
        }
        
    }

}
