package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.BookingStatusUpdateRequest;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Refund;
import com.pethealthcare.demo.response.MostUsedServiceResponse;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.repository.BookingDetailRepository;
import com.pethealthcare.demo.repository.BookingRepository;
import com.pethealthcare.demo.service.BookingDetailService;
import com.pethealthcare.demo.service.RefundService;
import com.pethealthcare.demo.service.ServiceSlotService;
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

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RefundService refundService;

    @Autowired
    private ServiceSlotService serviceSlotService;

    @GetMapping("/getAll")
    ResponseEntity<List<BookingDetail>> getAllBookingDetail() {
        return ResponseEntity.ok(bookingDetailService.getAllBookingDetail());
    }

    @GetMapping("/getAllBookingDetailNeedCage")
    List<BookingDetail> getAllBookingDetailByNeedCage() {
        return bookingDetailService.getAllBookingDetailNeedCage();
    }

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
    ResponseEntity<BookingDetail> getBookingByUserID(@PathVariable int id) {
        return ResponseEntity.ok(bookingDetailRepository.findBookingDetailByBookingDetailId(id));
    }


    @GetMapping("/getAllByBookingId/{BookingId}")
    ResponseEntity<List<BookingDetail>> getBookingByBookingID(@PathVariable int BookingId) {
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

    @GetMapping("/getAllBookingDetail_ByCusId")
    List<BookingDetail> getBookingDetailByCusid(@RequestParam int cusId) {
        return bookingDetailService.getBookingDetailByCus(cusId);
    }

    @PutMapping("/update/status/{bookingDetailId}")
    ResponseEntity<ResponseObject> updateBooking(@PathVariable int bookingDetailId, @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking updated successfully", bookingDetailService.updateStatusBookingDetail(bookingDetailId, request.getStatus()))
        );
    }

    @GetMapping("/getBookingDetailByStatus/{status}")
    ResponseEntity<List<BookingDetail>> getBookingDetailByStatus(@PathVariable String status) {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailByStatus(status));
    }

    @GetMapping("/getBookingDetailByCusIdStatus/{cusId}")
    ResponseEntity<List<BookingDetail>> getBookingDetailByCusIdStatus(@PathVariable int cusId) {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailByCus(cusId));
    }

    @GetMapping("/getBookingDetailByStayCage/")
    ResponseEntity<List<BookingDetail>> getBookingDetailByStayCage() {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailByStayCage());
    }

    @GetMapping("/cancelBookingDetail/{bookingDetailID}")
    ResponseEntity<ResponseObject> cancelBooking(@PathVariable int bookingDetailID) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailID);
        Booking booking = bookingRepository.findBookingByBookingId(bookingDetail.getBooking().getBookingId());
        if (bookingDetail.getStatus().equalsIgnoreCase("cancelled") || bookingDetail.getStatus().equalsIgnoreCase("completed")
                && booking.getStatus().equalsIgnoreCase("cancelled") || booking.getStatus().equalsIgnoreCase("completed")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "booking is already cancelled or completed", "")
            );
        } else if (booking.getStatus().equalsIgnoreCase("PROCESSING") || booking.getStatus().equalsIgnoreCase("Confirmed") || booking.getStatus().equalsIgnoreCase("PAID") && bookingDetail.getStatus().equalsIgnoreCase("WAITING")) {
            bookingDetailService.deleteBookingDetail(bookingDetailID);
            serviceSlotService.cancelSlot(bookingDetail.getUser().getUserId(), bookingDetail.getDate(), bookingDetail.getSlot().getSlotId());
            Refund refund = refundService.returnDepositCancelBookingDetail(bookingDetailID);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "booking deleted successfully", refund)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "booking status is not valid for cancellation", "")
        );
    }

    @PutMapping("/status/{bookingId}")
    public ResponseEntity<?> updateBookingStatus(@PathVariable int bookingId, @RequestParam String status) {
        bookingDetailService.updateStatusByBookingId(bookingId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getMostUsedServiceByMonthAndYear")
    MostUsedServiceResponse getMostUsedServiceByMonthAndYear(@RequestParam int month, @RequestParam int year) {
        return bookingDetailService.mostUsedService(month, year);
    }
}
