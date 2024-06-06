package com.pethealthcare.demo.controller;


import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.dto.request.PetUpdateRequest;
import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/pet")
public class PetController {
    @Autowired
    PetService petService;

    @GetMapping("/getAll")
    List<Pet> getAllPet() {
        return petService.getAllPet();
    }

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createPet(@RequestBody PetCreateRequest request) {
        Pet createdPet = petService.createPet(request);
        if (createdPet != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("ok", "Pet added successfully", createdPet)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Try again", "")
            );
        }
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updatePet(@PathVariable int id, @RequestBody PetUpdateRequest request) {
        Pet updatePet = petService.updatePet(id, request);
        if (updatePet != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "User updated successfully", updatePet)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "User not found", "")
            );
        }
    }


    @GetMapping("/getAll/{userID}")
    List<Pet> getPetsByUserID(@PathVariable int userID) {
        return petService.getPetsByUserID(userID);
    }





}
