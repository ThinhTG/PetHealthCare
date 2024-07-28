package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingStatusUpdateRequest;
import com.pethealthcare.demo.response.RevenueResponse;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.repository.BookingRepository;
import com.pethealthcare.demo.service.BookingService;
import com.pethealthcare.demo.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private RefundService refundService;
    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/getAll")
    List<Booking> getAllBooking() {
        return bookingService.getAllBooking();
    }

    @PostMapping("/add")
    ResponseEntity<ResponseObject> addBooking(@RequestBody BookingCreateRequest request) {
        Booking booking = bookingService.createBooking(request);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", "booking failed", null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking created successfully", booking)
        );
    }

    @GetMapping("/getAllByUserId/{userID}")
    List<Booking> getBookingByUserID(@PathVariable int userID) {
        return bookingService.getBookingsByUserID(userID);
    }

    @GetMapping("/api/revenue")
    public List<RevenueResponse> getRevenueByMonth(@RequestParam int year) {
        return bookingService.getRevenueByMonth(year);
    }

    @GetMapping("/getAllById/{status}")
    List<Booking> getBookingByStatus(@PathVariable String status) {
        return bookingService.getBookingsByStatus(status);
    }


    @PutMapping("/update/status/{bookingId}")
    ResponseEntity<ResponseObject> updateBooking(@PathVariable int bookingId, @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking updated successfully", bookingService.updateStatusBooking(bookingId, request.getStatus()))
        );
    }




    @GetMapping("/revenue-yearly")
    public ResponseEntity<Double> getYearlyRevenue(@RequestParam int year) {
        double revenue = bookingService.getRevenueByYear(year);
        return ResponseEntity.ok(revenue);
    }

}
