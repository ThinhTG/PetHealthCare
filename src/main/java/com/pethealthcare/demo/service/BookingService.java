package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.demo.dto.request.RevenueResponse;
import com.pethealthcare.demo.mapper.BookingDetailMapper;
import com.pethealthcare.demo.mapper.BookingMapper;
import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Booking> getAllBooking() {
        return bookingRepository.findAll();

    }


    public Booking createBooking(BookingCreateRequest request) {
        Booking newBooking = bookingMapper.toBooking(request);
        LocalDate localDate = LocalDate.now();
        newBooking.setDate(localDate);

        User user = userRepository.findUserByUserId(request.getCustomerId());

        newBooking.setUser(user);
        newBooking = bookingRepository.save(newBooking);

        for (BookingDetailCreateRequest request1 : request.getBookingDetails()) {
            BookingDetail bookingDetail = bookingDetailMapper.toBookingDetail(request1);
            bookingDetail.setDate(request1.getDate());
            bookingDetail.setBooking(newBooking);


            user = userRepository.findUserByUserId(request1.getVeterinarianId());
            bookingDetail.setUser(user);

            Pet pet = petRepository.findPetByPetId(request1.getPetId());
            bookingDetail.setPet(pet);

            Services services = serviceRepository.findByServiceId(request1.getServiceId());
            bookingDetail.setServices(services);

            Slot slot = slotRepository.findSlotBySlotId(request1.getSlotId());
            bookingDetail.setSlot(slot);

            serviceSlotService.bookedSlot(request1.getVeterinarianId(), request1.getDate(), request1.getSlotId());
            bookingDetailRepository.save(bookingDetail);
        }

        return newBooking;

    }

    public List<Booking> getBookingsByUserID(int userId) {
        User user = new User();
        user.setUserId(userId);
        return bookingRepository.getBookingByUser(user);
    }


//    public void deleteBooking(int bookingId, BookingCancelRequest request) {
//        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
//        booking.setStatus("CANCELLED");
//        paymentService.returnDeposit(bookingId, request);
//
//    }

    public Booking updateStatusBooking(int bookingId, String status) {
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public double getRevenueByDate(LocalDate date) {
        LocalDate startOfDay = date;
        LocalDate endOfDay = date.plusDays(1);
        return calculateRevenue(startOfDay, endOfDay);
    }

    public double getRevenueByMonth(int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        return calculateRevenue(startOfMonth, endOfMonth);
    }

    public double getRevenueByYear(int year) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);
        return calculateRevenue(startOfYear, endOfYear);
    }

    private double calculateRevenue(LocalDate startDate, LocalDate endDate) {
        List<Booking> bookings = bookingRepository.findByDateBetween(startDate, endDate);
        return bookings.stream().mapToDouble(Booking::getTotalPrice).sum();
    }

    public List<RevenueResponse> getRevenueByMonth(int year) {
        return bookingRepository.getRevenueByMonth(year);
    }




    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }
}
