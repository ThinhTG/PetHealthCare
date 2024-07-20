package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.dto.request.BookingStatusUpdateRequest;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.responsitory.BookingDetailRepository;
import com.pethealthcare.demo.responsitory.BookingRepository;
import com.pethealthcare.demo.service.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/bookingDetail")
public class BookingDetailController {
    @Autowired
    private BookingDetailService bookingDetailService;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    @GetMapping("/getAll")
    ResponseEntity<List<BookingDetail>> getAllBookingDetail() {
        return ResponseEntity.ok(bookingDetailService.getAllBookingDetail());
    }

    @GetMapping("/getAllBookingDetailNeedCage")
    List<BookingDetail> getAllBookingDetailByNeedCage(){return bookingDetailService.getAllBookingDetailNeedCage();}

    @PutMapping("/needCage/{bookingDetailId}")
    ResponseEntity<ResponseObject> updateNeedcage(@PathVariable int bookingDetailId) {
        BookingDetail updateNeedCage = bookingDetailService.updateNeedCage(bookingDetailId);
        if (updateNeedCage != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "need cage now iss true", updateNeedCage)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "bookingID is notfound", "")
            );
        }
    }

    @GetMapping("/getAllById/{id}")
  ResponseEntity<BookingDetail>  getBookingByUserID(@PathVariable int id) {
        return  ResponseEntity.ok(bookingDetailRepository.findBookingDetailByBookingDetailId(id));
    }


    @GetMapping("/getAllByBookingId/{BookingId}")
    ResponseEntity<List<BookingDetail>>  getBookingByBookingID(@PathVariable int BookingId) {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailByBookingId(BookingId));
    }

    @GetMapping("/getByNeedCage")
    List<BookingDetail> getBookingDetailByNeedCage() {
        return bookingDetailService.getBookingDetailByNeedCage();
    }

    @GetMapping("/getByDate")
    List<BookingDetail> getBookingDetailByDate(@RequestParam Date date) {
        return bookingDetailService.getBookingDetailByDate(date);
    }


    @GetMapping("/getAllBookingDetail_ByUserId")
    List<BookingDetail> getBookingDetailByUserid(@RequestParam int userId) {
        return bookingDetailService.getBookingDetailByUser(userId);
    }

    @PutMapping("/update/status/{bookingDetailId}")
    ResponseEntity<ResponseObject> updateBooking(@PathVariable int bookingDetailId, @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking updated successfully", bookingDetailService.updateStatusBookingDetail(bookingDetailId, request.getStatus()))
        );
    }



}
