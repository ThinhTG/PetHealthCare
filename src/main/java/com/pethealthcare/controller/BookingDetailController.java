package com.pethealthcare.controller;


import com.pethealthcare.model.BookingDetail;
import com.pethealthcare.service.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
