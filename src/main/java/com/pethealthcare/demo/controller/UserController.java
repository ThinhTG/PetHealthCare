package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.*;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.service.EmailService;
import com.pethealthcare.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createUser(@RequestBody UserCreateRequest request) {
        User createdUser = userService.createUser(request);
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


    @PostMapping("/createmultirole")
    ResponseEntity<ResponseObject> createAcc(@RequestBody AccCreateRequest request) {
        User createdUser = userService.createAcc(request);
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
    ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return new ResponseEntity<>(userService.forgotPassword(request.getEmail()), HttpStatus.OK);
    }
    @PutMapping("/verify-otp")
    ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        return new ResponseEntity<>(userService.checkOtp(request.getEmail(), request.getOtp()), HttpStatus.OK);
    }

    @PutMapping("/reset-pasword")
    ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return new ResponseEntity<>(userService.resetPassword(request.getEmail(), request.getPassword()), HttpStatus.OK);
    }
    @GetMapping("/getAll")
    List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateUser(@PathVariable int id, @RequestBody UserUpdateRequest request) {
        User updateUser = userService.updateUser(id, request);
        if (updateUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "User updated successfully", updateUser)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "User not found", "")
            );
        }
    }

    @GetMapping("/getaccount/{id}")
    User getUser(@PathVariable int id) {
    return userService.getAccountById(id);
    }

    @GetMapping("/getVeterinarian")
    List<User> getVeterinarian (){
        return userService.getAllVeterinarians();
    }

    @PutMapping("/manageRole/{userID}")
    ResponseEntity<ResponseObject> updateUserRole(@PathVariable int userID,@RequestBody UserRoleUpdateRequest request) {
        User updateUser = userService.updateUserRole(userID, request.getNewrole());
        if (updateUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "User updated successfully", updateUser)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "User not found", "")
            );
        }
    }





}
