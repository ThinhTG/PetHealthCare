package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.AuthenticationRequest;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/login")
    ResponseEntity<ResponseObject> login(@RequestBody AuthenticationRequest request) {
        String auth = authenticationService.authenticate(request);
        if (auth != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Login successfully", auth)
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseObject("400", "Email or Password is incorrect")
            );
        }
    }
}
