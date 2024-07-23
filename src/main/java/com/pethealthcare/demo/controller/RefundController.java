package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.Refund;
import com.pethealthcare.demo.service.PetService;
import com.pethealthcare.demo.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/refund")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @GetMapping("/getRefund")
    List<Refund> getAllRefund() {
        return refundService.getReturn();
    }

    @GetMapping("/getRefundByUserId/{userId}")
    List<Refund> getRefundByUserId(@PathVariable int userId) {
        return refundService.getReturnByCusId(userId);
    }

    @GetMapping("/getRefundByBookingDetailId/{bookingDetailId}")
    List<Refund> getRefundByBookingDetailId(@PathVariable int bookingDetailId) {
        return refundService.getReturnByBookingDetailId(bookingDetailId);
    }

}
