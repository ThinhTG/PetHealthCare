package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/get-by-user")
    public ResponseEntity<ResponseObject> getWalletByUser(@RequestParam int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Success", walletService.getWallet(userId))
        );
    }

}
