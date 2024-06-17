package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.CageUpdateRequest;
import com.pethealthcare.demo.dto.request.ForgotPasswordRequest;
import com.pethealthcare.demo.model.Cage;
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
    ResponseEntity<ResponseObject> createCage() {
        Cage cage = cageService.createCage();
        if (cage == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("ok", "Cage create failed", cage));
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cage create successfully", cage));
    }

    @PutMapping("/update")
    ResponseEntity<ResponseObject> updateCage(@RequestBody CageUpdateRequest cageUpdateRequest){
        Cage updateCage = cageService.updateCage(cageUpdateRequest.getCageId(), cageUpdateRequest.isStatus());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("oke", "Update successfully", updateCage)
        );
    }

    @GetMapping("/getall")
    ResponseEntity<ResponseObject> getAll(){

         return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Cage create successfully", cageService.getALl()));

    }


}
