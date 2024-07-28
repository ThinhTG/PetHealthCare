package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.enums.BookingStatus;
import com.pethealthcare.demo.enums.ServiceSlotStatus;
import com.pethealthcare.demo.response.RevenueResponse;
import com.pethealthcare.demo.mapper.BookingMapper;
import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private ServiceSlotService serviceSlotService;

    @Autowired
    private UserRepository userRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @Autowired
    private BookingDetailService bookingDetailService;

    public List<Booking> getAllBooking() {
        return bookingRepository.findAll();

    }

    public Booking createBooking(BookingCreateRequest request) {
        if (request.getBookingDetails().isEmpty()) {
            return null;
        }
        for (BookingDetailCreateRequest bookingDetail : request.getBookingDetails()) {
            if (bookingDetailRepository.existsByServices_ServiceIdAndAndSlot_SlotIdAndDateAndUser_UserId(bookingDetail.getServiceId(),
                    bookingDetail.getSlotId(),
                    bookingDetail.getDate(),
                    bookingDetail.getVeterinarianId())) {
                return null;
            }
        }

        Booking newBooking = bookingMapper.toBooking(request);
        LocalDate localDate = LocalDate.now();
        newBooking.setDate(localDate);

        User user = userRepository.findUserByUserId(request.getCustomerId());
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setUser(user);
        newBooking = bookingRepository.save(newBooking);

        for (BookingDetailCreateRequest request1 : request.getBookingDetails()) {
            bookingDetailService.createBookingDetail(request1, newBooking);
        }

        scheduleBookingStatusCheck(newBooking.getBookingId());

        return newBooking;

    }

    private void scheduleBookingStatusCheck(int bookingId) {
        scheduler.schedule(() -> {
            Booking booking = bookingRepository.findById(bookingId).orElse(null);
            if (booking != null && booking.getStatus() == BookingStatus.PENDING) {
                booking.setStatus(BookingStatus.CANCELLED);
                List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailsByBooking(booking);
                bookingRepository.save(booking);
                for (BookingDetail bookingDetail : bookingDetails) {
                    bookingDetail.setStatus(BookingDetailStatus.CANCELLED);
                    bookingDetailRepository.save(bookingDetail);
                    serviceSlotService.updateServiceSlotStatus(bookingDetail.getUser().getUserId(),
                            bookingDetail.getDate(),
                            bookingDetail.getSlot().getSlotId(),
                            ServiceSlotStatus.AVAILABLE);
                }
            }
        }, 15, TimeUnit.MINUTES);
    }

    public List<Booking> getBookingsByUserID(int userId) {
        User user = new User();
        user.setUserId(userId);
        return bookingRepository.getBookingByUser(user);
    }

    public Booking updateStatusBooking(int bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findBookingByBookingId(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
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
