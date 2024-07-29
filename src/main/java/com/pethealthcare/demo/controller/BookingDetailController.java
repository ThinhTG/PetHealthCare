package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.BookingDetailStatusUpdateRequest;
import com.pethealthcare.demo.dto.request.BookingDetailUpdateRequest;
import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.enums.BookingStatus;
import com.pethealthcare.demo.model.*;
import com.pethealthcare.demo.repository.PetRepository;
import com.pethealthcare.demo.repository.UserRepository;
import com.pethealthcare.demo.response.MostUsedServiceResponse;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.repository.BookingDetailRepository;
import com.pethealthcare.demo.repository.BookingRepository;
import com.pethealthcare.demo.service.BookingDetailService;
import com.pethealthcare.demo.service.RefundService;
import com.pethealthcare.demo.service.ServiceSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAll")
    ResponseEntity<List<BookingDetail>> getAllBookingDetail() {
        return ResponseEntity.ok(bookingDetailService.getAllBookingDetail());
    }

    @GetMapping("/getAllBookingDetailNeedCage")
    List<BookingDetail> getAllBookingDetailByNeedCage() {
        return bookingDetailService.getAllBookingDetailNeedCage();
    }

    @PutMapping("/needCage/{bookingDetailId}")
    ResponseEntity<ResponseObject> updateNeedCage(@PathVariable int bookingDetailId) {
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
    List<BookingDetail> getBookingDetailByCustomerId(@RequestParam int cusId) {
        return bookingDetailService.getBookingDetailByCus(cusId);
    }

    @GetMapping("/getAllBookingDetail_ByPhoneNumberAndDate")
    ResponseEntity<ResponseObject> getBookingDetailByPhone(@RequestParam String phone,
                                                           @RequestParam @DateTimeFormat(pattern = "MM-dd-yyyy") LocalDate date) {
        List<BookingDetail> bookingDetails = bookingDetailService.getBookingDetailByPhone(phone, date);
        if (bookingDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("ok", "BookingDetail not found by this phone number")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Find successfully", bookingDetails)
        );
    }

    @PutMapping("/update/status/{bookingDetailId}")
    ResponseEntity<ResponseObject> updateBooking(@PathVariable int bookingDetailId, @RequestBody BookingDetailStatusUpdateRequest request) {
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

    @GetMapping("/getBookingDetailByPetIsDeleted/{petId}")
    ResponseEntity<List<BookingDetail>> getBookingDetailByPetIsDeleted(@PathVariable int petId) {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailByPetIsDeleted(petId));
    }

    @GetMapping("/getBookingDetailStatusByVet/{vetId}")
    ResponseEntity<List<BookingDetail>> getBookingDetailStatusByVet(@PathVariable int vetId) {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailStatusByVet(vetId));
    }

    @GetMapping("/getBookingDetailByVetCancel")
    ResponseEntity<List<BookingDetail>> getBookingDetailByVetCancelled() {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailByVetCancel());
    }

    @GetMapping("/getAllBookingDetailNonConfirmed")
    ResponseEntity<List<BookingDetail>> getBookingDetailNonConfirmed() {
        return ResponseEntity.ok(bookingDetailService.getBookingDetailNonConfirmed());
    }

    @GetMapping("/cancelBookingDetailByPet/")
    public ResponseEntity<ResponseObject> cancelBookingDetailByPet(@RequestParam int petId, @RequestParam int userId) {
        Pet pet = petRepository.findPetByPetId(petId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailByPet(pet);
        List<Refund> refunds = new ArrayList<>();
        boolean anyCancellable = false;

        for (BookingDetail bookingDetail : bookingDetails) {
            BookingDetailStatus status = bookingDetail.getStatus();

            if (status == BookingDetailStatus.WAITING || status == BookingDetailStatus.CONFIRMED) {
                bookingDetailService.deleteBookingDetail(bookingDetail.getBookingDetailId());
                serviceSlotService.cancelSlot(bookingDetail.getUser().getUserId(), bookingDetail.getDate(), bookingDetail.getSlot().getSlotId());
                Refund refund = refundService.returnDepositCancelBookingDetail(bookingDetail.getBookingDetailId(), userId);
                refunds.add(refund);
                anyCancellable = true;
            }
        }

        if (anyCancellable) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Bookings deleted successfully", refunds)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "No booking found with valid status for cancellation", "")
            );
        }
    }

    @GetMapping("/cancelBookingDetail/")
    ResponseEntity<ResponseObject> cancelBookingDetail(@RequestParam int bookingDetailID, @RequestParam int userId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailID);
        Booking booking = bookingRepository.findBookingByBookingId(bookingDetail.getBooking().getBookingId());
        if (bookingDetail.getStatus() == BookingDetailStatus.CANCELLED || bookingDetail.getStatus() == BookingDetailStatus.COMPLETED
                && booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "booking is already cancelled or completed", "")
            );

        } else if (booking.getStatus() == BookingStatus.PAID && bookingDetail.getStatus() == BookingDetailStatus.CONFIRMED || bookingDetail.getStatus() == BookingDetailStatus.WAITING) {

            bookingDetailService.deleteBookingDetail(bookingDetailID);
            serviceSlotService.cancelSlot(bookingDetail.getUser().getUserId(), bookingDetail.getDate(), bookingDetail.getSlot().getSlotId());
            Refund refund = refundService.returnDepositCancelBookingDetail(bookingDetailID, userId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "booking deleted successfully", refund)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "booking status is not valid for cancellation", "")
        );
    }

    @GetMapping("/staffCancelBookingDetail/")
    ResponseEntity<ResponseObject> staffCancelBookingDetail(@RequestParam int bookingDetailID, @RequestParam int userId) {
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailID);
        Booking booking = bookingRepository.findBookingByBookingId(bookingDetail.getBooking().getBookingId());
        if (bookingDetail.getStatus() == BookingDetailStatus.CANCELLED || bookingDetail.getStatus() == BookingDetailStatus.COMPLETED
                && booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "booking/bookingDetail is already cancelled or completed or pending", "")
            );

        } else if (booking.getStatus() == BookingStatus.PAID && bookingDetail.getStatus() == BookingDetailStatus.CONFIRMED || bookingDetail.getStatus() == BookingDetailStatus.WAITING) {

            bookingDetailService.deleteBookingDetail(bookingDetailID);
            serviceSlotService.cancelSlot(bookingDetail.getUser().getUserId(), bookingDetail.getDate(), bookingDetail.getSlot().getSlotId());
            Refund refund = refundService.returnDepositStaffCancelBookingDetail(bookingDetailID, userId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "booking deleted successfully", refund)
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "booking status is not valid for cancellation", "")
        );
    }

    @GetMapping("/vetCancelBookingDetail/")
    public ResponseEntity<ResponseObject> vetCancelBookingDetail(@RequestParam LocalDate dateTime, @RequestParam int vetId) {
        User user = userRepository.findUserByUserId(vetId);
        List<BookingDetail> bookingDetails = bookingDetailRepository.getBookingDetailByuser(user);

        boolean updated = false;

        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.getStatus() == BookingDetailStatus.CANCELLED || bookingDetail.getStatus() == BookingDetailStatus.COMPLETED) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("failed", "One or more booking details are already cancelled or completed", "")
                );
            } else if (bookingDetail.getStatus() == BookingDetailStatus.CONFIRMED || bookingDetail.getStatus() == BookingDetailStatus.WAITING) {
                bookingDetailService.updateStatusBookingDetailVetCancel(dateTime);
                updated = true;
            }
        }

        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Booking details updated successfully", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "No booking details were updated", "")
            );
        }
    }

    @PutMapping("/update/bookingDetail")
    ResponseEntity<ResponseObject> updateBookingDetail(@RequestBody BookingDetailUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking updated successfully", bookingDetailService.updateBookingDetail(request))
        );
    }

    @PutMapping("/update/bookingDetailVetCancel/{bookingDetailId}")
    ResponseEntity<ResponseObject> updateBookingDetailVetCancel(@PathVariable int bookingDetailId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "booking updated successfully", bookingDetailService.updateVetCancel(bookingDetailId))
        );
    }

    @PutMapping("/status/{bookingId}")
    public ResponseEntity<?> updateBookingStatus(@PathVariable int bookingId, @RequestParam BookingDetailStatus status) {
        bookingDetailService.updateStatusByBookingId(bookingId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getMostUsedServiceByMonthAndYear")
    MostUsedServiceResponse getMostUsedServiceByMonthAndYear(@RequestParam int month, @RequestParam int year) {
        return bookingDetailService.mostUsedService(month, year);
    }
}
