package com.pethealthcare.demo.controller;


import com.pethealthcare.demo.dto.request.CageCreateRequest;
import com.pethealthcare.demo.dto.request.CheckingCageRequest;
import com.pethealthcare.demo.dto.request.CheckoutCageRequest;
import com.pethealthcare.demo.dto.request.ForgotPasswordRequest;
import com.pethealthcare.demo.model.Cage;
import com.pethealthcare.demo.model.CageType;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.service.CageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cage")
public class CageController {

    @Autowired
    private CageService cageService;

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createCage(@RequestBody CageCreateRequest request) {
        Cage cage = cageService.createCage(request.getType());
        if (cage == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("ok", "Cage create failed", cage));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cage create successfully", cage));
    }

    @GetMapping("/getall")
    ResponseEntity<ResponseObject> getAll(){

         return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cage create successfully", cageService.getALl()));

    }

    @GetMapping("/getAvailableByType")
    ResponseEntity<ResponseObject> getAvailableByType(@RequestParam CageType type){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cage create successfully", cageService.getCageAvailableByType(type)));
    }

    @PostMapping("/checkin")
    ResponseEntity<ResponseObject> checking(@RequestBody CheckingCageRequest checkingCage){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cage create successfully", cageService.checkinCage(checkingCage.getCageId(), checkingCage.getBookingDetailId())));
    }


    @PostMapping("/checkout")
    ResponseEntity<ResponseObject> checkout(@RequestBody CheckoutCageRequest checkoutCage){
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cage create successfully", cageService.checkoutCage(checkoutCage.getCageId())));
    }

    @GetMapping("/getHasPetByType")
    ResponseEntity<ResponseObject> getHasPetByType() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cages with pets of type " ,
                        cageService.getCageHasPetByType()));
    }

}
