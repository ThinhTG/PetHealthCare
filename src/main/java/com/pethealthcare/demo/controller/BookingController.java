package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.BookingCancelRequest;
import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.service.BookingService;
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

    @GetMapping("/getAll")
    List<Booking> getAllBooking() {
        return bookingService.getAllBooking();
    }

    @PostMapping("/add")
    ResponseEntity<ResponseObject> addBooking(@RequestBody BookingCreateRequest request) {
        bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking created successfully")
        );
    }

    @GetMapping("/getAllById/{userID}")
    List<Booking> getBookingByUserID(@PathVariable int userID) {
        return bookingService.getBookingsByUserID(userID);
    }

    @PostMapping("/delete/{bookingID}")
ResponseEntity<ResponseObject> deleteBooking(@PathVariable int bookingID, BookingCancelRequest request) {
        bookingService.deleteBooking(bookingID, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking deleted successfully")
        );
    }


}
