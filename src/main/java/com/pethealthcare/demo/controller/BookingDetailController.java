package com.pethealthcare.demo.controller;


import com.pethealthcare.demo.dto.request.BookingDetailByDateRequest;
import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.service.BookingDetailService;
import com.pethealthcare.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookingDetail")
public class BookingDetailController {
    @Autowired
    private BookingDetailService bookingDetailService;

    @GetMapping("/getAll")
    List<BookingDetail> getAllBookingDetail() {
        return bookingDetailService.getAllBookingDetail();
    }

    @GetMapping("/getByNeedCage")
    List<BookingDetail> getBookingDetailByNeedCage() {
        return bookingDetailService.getBookingDetailByNeedCage();
    }
    @GetMapping("/getByDate")
    List<BookingDetail> getBookingDetailByDate(@RequestParam BookingDetailByDateRequest request) {
        return bookingDetailService.getBookingDetailByDate(request.getDate());
    }

}
