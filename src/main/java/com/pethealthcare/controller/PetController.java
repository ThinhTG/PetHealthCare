package com.pethealthcare.controller;


import com.pethealthcare.dto.request.PetCreateRequest;
import com.pethealthcare.dto.request.PetUpdateRequest;
import com.pethealthcare.model.Pet;
import com.pethealthcare.model.ResponseObject;
import com.pethealthcare.model.User;
import com.pethealthcare.service.PetService;
import com.pethealthcare.service.UserService;
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
    @Autowired
    UserService userService;

    @GetMapping("/getAll")
    List<Pet> getAllPet() {
        return petService.getAllPet();
    }

    @PostMapping("/create/{id}")
    ResponseEntity<ResponseObject> createPet(@PathVariable int id,@RequestBody PetCreateRequest request) {
        Pet createdPet = petService.createPet(id,request);
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

    @PutMapping("/update/{userid}/{petid}")
    ResponseEntity<ResponseObject> updatePet(@PathVariable int userid,@PathVariable int petid, @RequestBody PetUpdateRequest request) {
        Pet updatePet = petService.updatePet(userid,petid, request);
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

    @DeleteMapping("/deletePet/{userid}/{petid}")
    ResponseEntity<ResponseObject> deletePet(@PathVariable int userid,@PathVariable int petid) {
        User existsUser = userService.getAccountById(userid);
        if (existsUser != null) {
            boolean existsPet = petService.findPetByID(petid);
            if (existsPet) {
                petService.deletePetByID(petid);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete pet Successfully", "")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Pet not found", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "User not found", "")
        );
    }







}
