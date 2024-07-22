package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.BookingCancelRequest;
import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingStatusUpdateRequest;
import com.pethealthcare.demo.dto.request.RevenueResponse;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.responsitory.BookingRepository;
import com.pethealthcare.demo.service.BookingService;
import com.pethealthcare.demo.service.PaymentService;
import com.pethealthcare.demo.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
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

//    @PostMapping("/delete/{bookingID}")
//    ResponseEntity<ResponseObject> deleteBooking(@PathVariable int bookingID, BookingCancelRequest request) {
//        bookingService.deleteBooking(bookingID, request);
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok", "booking deleted successfully")
//        );
//    }

//    @GetMapping("/cancel/{bookingID}")
//    ResponseEntity<ResponseObject> cancelBooking(@PathVariable int bookingID) {
//        Booking booking = bookingRepository.findBookingByBookingId(bookingID);
//        if (booking.getStatus().equalsIgnoreCase("CANCELLED") || booking.getStatus().equalsIgnoreCase("COMPLETED")) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    new ResponseObject("failed", "booking is already cancelled or completed", "")
//            );
//        } else if (booking.getStatus().equalsIgnoreCase("PAID")) {
//            bookingService.deleteBooking(bookingID);
//            refundService.returnDepositCancelBooking(bookingID);
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("ok", "booking deleted successfully", refundService.returnDepositCancelBooking(bookingID))
//            );
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    new ResponseObject("failed", "booking status is not valid for cancellation", "")
//            );
//        }
//    }
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
