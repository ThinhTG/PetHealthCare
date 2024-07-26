package com.pethealthcare.demo.controller;


import com.pethealthcare.demo.dto.request.*;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.repository.PetRepository;
import com.pethealthcare.demo.repository.UserRepository;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.service.PetService;
import com.pethealthcare.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAll")
    List<Pet> getAllPet() {
        return petService.getAllPet();
    }

    @PostMapping("/create/{id}")
    ResponseEntity<ResponseObject> createPet(@PathVariable int id, @RequestBody PetCreateRequest request, @RequestParam MultipartFile file) throws IOException {
        Pet createdPet = petService.createPet(id, request, file);
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


    @GetMapping("/getPet/{userID})")
    List<Pet> getPet(@PathVariable int userID) {
        User user = userRepository.findUserByUserId(userID);
        return petRepository.getPetByIsDeleted(false,user);
    }


    @PutMapping("/update/{petid}")
    ResponseEntity<ResponseObject> updatePet(@PathVariable int petid, @RequestBody PetUpdateRequest request, @RequestParam MultipartFile file) throws IOException {
        Pet updatePet = petService.updatePet(petid, request, file);

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
