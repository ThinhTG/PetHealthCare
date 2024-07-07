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

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<ResponseObject> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        int amount = Integer.parseInt(request.getParameter("vnp_Amount"));
        String bankCode = request.getParameter("vnp_BankCode");
        int paymentId = Integer.parseInt(request.getParameter("vnp_TransactionNo"));
        String cardType = request.getParameter("vnp_CardType");
        String bankTranNo = request.getParameter("vnp_BankTranNo");
        String payDate = request.getParameter("vnp_PayDate");
        String orderInfo = request.getParameter("vnp_OrderInfo");

        if (status.equals("00")) {
            Payment payment = paymentService.createPayment(paymentId, amount, bankCode,
                    bankTranNo, cardType, payDate, orderInfo);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Success", payment)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Bad Request", "Failed", null)
            );
        }
    }
}
