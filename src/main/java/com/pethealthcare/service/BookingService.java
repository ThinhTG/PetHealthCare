package com.pethealthcare.service;

import com.pethealthcare.dto.request.BookingCreateRequest;
import com.pethealthcare.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.mapper.BookingDetailMapper;
import com.pethealthcare.mapper.BookingMapper;
import com.pethealthcare.model.*;
import com.pethealthcare.responsitory.*;
import com.pethealthcare.model.*;
import com.pethealthcare.responsitory.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SlotRepository slotRepository;

    public  List<Booking> getAllBooking() {
        return bookingRepository.findAll();
    }


    @Transactional
    public void createBooking(BookingCreateRequest request) {
        Booking newBooking = bookingMapper.toBooking(request);
        newBooking.setDate(new Date());

        User user = userRepository.findUserByUserId(request.getCustomerId());

        newBooking.setUser(user);
        newBooking = bookingRepository.save(newBooking);

        for (BookingDetailCreateRequest request1 : request.getBookingDetails()) {
            BookingDetail bookingDetail = bookingDetailMapper.toBookingDetail(request1);
            bookingDetail.setBooking(newBooking);


            user = userRepository.findUserByUserId(request1.getVeterinarianId());
            bookingDetail.setUser(user);

            Pet pet = petRepository.findPetByPetId(request1.getPetId());
            bookingDetail.setPet(pet);

            Services services = serviceRepository.findByServiceId(request1.getServiceId());
            bookingDetail.setServices(services);

            Slot slot  = slotRepository.findSlotBySlotId(request1.getSlotId());
            bookingDetail.setSlot(slot);

            serviceSlotService.bookedSlot(request1.getVeterinarianId(), request1.getDate(), request1.getSlotId());
            bookingDetailRepository.save(bookingDetail);
        }
        
    }

    public List<Booking> getBookingsByUserID(int userId) {
        User user = new User();
        user.setUserId(userId);
        return bookingRepository.getBookingByUser(user);
    }

}
