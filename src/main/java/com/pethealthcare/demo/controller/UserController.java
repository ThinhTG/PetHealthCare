package com.pethealthcare.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pethealthcare.demo.dto.request.*;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    ResponseEntity<ResponseObject> createAcc(@RequestParam("request") String requestJson,
                                             @RequestParam(required = false) MultipartFile file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AccCreateRequest request = objectMapper.readValue(requestJson, AccCreateRequest.class);
        User createdUser = userService.createAcc(request, file);
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


    @GetMapping("/getVeterinarianActive")
    List<User> getVeterinarianActive() {
        return userService.getAllVeterinariansActive();
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

    @PutMapping("/update/{userId}")
    ResponseEntity<ResponseObject> updateUser(@PathVariable int userId,
                                              @RequestParam("request") String requestJson,
                                              @RequestParam(required = false) MultipartFile file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserUpdateRequest request = objectMapper.readValue(requestJson, UserUpdateRequest.class);
        String updateUser = userService.updateUser(userId, request, file);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", updateUser, "")
        );
    }

    @GetMapping("/getaccount/{id}")
    User getUser(@PathVariable int id) {
        return userService.getAccountById(id);
    }

    @GetMapping("/getVeterinarian")
    List<User> getVeterinarians() {
        return userService.getAllVeterinarians();
    }

    @PutMapping("/manageRole/{userID}")
    ResponseEntity<ResponseObject> updateUserRole(@PathVariable int userID, @RequestBody UserRoleUpdateRequest request) {
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

    @DeleteMapping("/delete/{id}")
    ResponseEntity<ResponseObject> deleteUser(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "User deleted successfully", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "User not found or booked", "")
            );
        }
    }


}
