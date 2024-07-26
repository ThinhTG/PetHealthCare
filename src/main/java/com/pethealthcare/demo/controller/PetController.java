package com.pethealthcare.demo.controller;


import com.pethealthcare.demo.dto.request.*;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.repository.PetRepository;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.service.PetService;
import com.pethealthcare.demo.service.UserService;
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

    @Autowired
    private PetRepository petRepository;

    @GetMapping("/getAll")
    List<Pet> getAllPet() {
        return petService.getAllPet();
    }

    @PostMapping("/create/{id}")
    ResponseEntity<ResponseObject> createPet(@PathVariable int id, @RequestBody PetCreateRequest request) {
        Pet createdPet = petService.createPet(id, request);
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
    ResponseEntity<ResponseObject> updatePet(@PathVariable int userid, @PathVariable int petid, @RequestBody PetUpdateRequest request) {
        Pet updatePet = petService.updatePet(userid, petid, request);
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
    ResponseEntity<ResponseObject> deletePet(@PathVariable Integer userid, @PathVariable Integer petid) {
        if (userid == null || petid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "User ID or Pet ID is missing", "")
            );
        }

        User existsUser = userService.getAccountById(userid);
        if (existsUser != null) {
            boolean existsPet = petService.findPetByID(petid);
            if (existsPet) {
                Pet pet = petRepository.findPetByPetId(petid);
                if (!pet.isDeleted()) {
                    petService.deletePetByID(petid);
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Delete pet successfully", "")
                    );
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                            new ResponseObject("failed", "Pet already deleted", "")
                    );
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Pet not found", "")
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "User not found", "")
            );
        }
    }


    @PutMapping("/update-Vaccination")
    ResponseEntity<ResponseObject> updateVaccination(@RequestBody PetUpdateVacionationRequest request) {
        petService.updateVaccination(request.getPetId(), request.getVaccination());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Vaccination updated successfully"));
    }
}
