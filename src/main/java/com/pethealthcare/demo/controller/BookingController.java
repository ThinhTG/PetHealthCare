package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingStatusUpdateRequest;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.dto.response.ResponseObject;
import com.pethealthcare.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

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

    @GetMapping("/getAllById/{userID}")
    List<Booking> getBookingByUserID(@PathVariable int userID) {
        return bookingService.getBookingsByUserID(userID);
    }


//    @PostMapping("/delete/{bookingID}")
//    ResponseEntity<ResponseObject> deleteBooking(@PathVariable int bookingID, BookingCancelRequest request) {
//        bookingService.deleteBooking(bookingID, request);
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok", "booking deleted successfully")
//        );
//    }


    @PutMapping("/update/status/{bookingId}")
    ResponseEntity<ResponseObject> updateBooking(@PathVariable int bookingId, @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking updated successfully", bookingService.updateStatusBooking(bookingId, request.getStatus()))
        );
    }

    @GetMapping("/revenue-daily")
    public ResponseEntity<Double> getDailyRevenue(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        double revenue = bookingService.getRevenueByDate(date);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue-monthly")
    public ResponseEntity<Double> getMonthlyRevenue(@RequestParam int year, @RequestParam int month) {
        double revenue = bookingService.getRevenueByMonth(year, month);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue-yearly")
    public ResponseEntity<Double> getYearlyRevenue(@RequestParam int year) {
        double revenue = bookingService.getRevenueByYear(year);
        return ResponseEntity.ok(revenue);
    }

}
