package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.OtpRequest;
import com.pethealthcare.demo.dto.request.ResetPasswordRequest;
import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.service.EmailService;
import com.pethealthcare.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/account")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    ResponseEntity<ResponseObject> register(@RequestBody UserCreateRequest request) {
        User createdUser = userService.register(request);
        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("ok", "Account created successfully", createdUser)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Email address already used", "")
            );
        }
    }

    @PutMapping("/forgot-password")
    ResponseEntity<String> forgotPassword(@RequestBody String email) {
        return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
    }

    @PutMapping("/verify-otp")
    ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        return new ResponseEntity<>(userService.checkOtp(request.getEmail(), request.getOtp()), HttpStatus.OK);
    }

    @PutMapping("/reset-pasword")
    ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseEntity<>(userService.resetPassword(request.getEmail(), request.getPassword()), HttpStatus.OK);
    }

}
