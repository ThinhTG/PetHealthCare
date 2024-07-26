package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.TransactionCreateRequest;
import com.pethealthcare.demo.model.Transaction;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/vn-pay")
    public ResponseEntity<ResponseObject> pay(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Success", transactionService.createVnPayPayment(request))
        );
    }

    @PostMapping("/deposit")
    public ResponseEntity<ResponseObject> deposit(@RequestParam int amount,
                                                             @RequestParam String vnpPayDate, @RequestParam String orderInfo) {
        Transaction transaction = transactionService.deposit(amount, vnpPayDate, orderInfo);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Success", transaction)
        );
    }

    @PostMapping("/pay-booking")
    public ResponseEntity<ResponseObject> payBooking(@RequestBody TransactionCreateRequest request) {
        String result = transactionService.payBooking(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Success", result)
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseObject> getAllTransaction(@RequestParam int walletId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Success", transactionService.getTransactionsByWalletId(walletId))
        );
    }
}
