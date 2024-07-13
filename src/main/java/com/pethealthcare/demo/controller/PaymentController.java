package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.PaymentCreateRequest;
import com.pethealthcare.demo.model.Payment;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;


@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ResponseEntity<ResponseObject> pay(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Success", paymentService.createVnPayPayment(request))
        );
    }


//    @GetMapping("/revenue")
//    ResponseEntity<ResponseObject> getRevenue() {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok", "Get revenue successfully", paymentService.getRevenue())
//        );
//    }
//    @GetMapping("/revenueInPeriod")
//    ResponseEntity<ResponseObject> getRevenueInPeriod(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok", "Get revenue successfully", paymentService.getRevenueInPeriod(startDate, endDate))
//        );
//    }


    @GetMapping("/create-payment")
    public ResponseEntity<ResponseObject> payCallbackHandler(@RequestParam int transactionNo, @RequestParam int amount,
                                                             @RequestParam String bankCode, @RequestParam String bankTranNo,
                                                             @RequestParam String cardType, @RequestParam String vnpPayDate,
                                                             @RequestParam String orderInfo, @RequestParam int txnRef) {
        Payment payment = paymentService.createPayment(transactionNo, amount, bankCode, bankTranNo,
                cardType, vnpPayDate, orderInfo, txnRef);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Success", payment)
        );
    }
}
