package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.PaymentCreateRequest;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createPayment(@RequestBody PaymentCreateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Payment create successfully", paymentService.createPayment(request))
        );
    }
}
