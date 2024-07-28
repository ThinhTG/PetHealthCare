package com.pethealthcare.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
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
    public ResponseEntity<ResponseObject> createPet(
            @PathVariable int id,
            @RequestParam("request") String requestJson,
            @RequestParam(required = false) MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PetCreateRequest request = objectMapper.readValue(requestJson, PetCreateRequest.class);
        System.out.println(file);
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


    @GetMapping("/getPet/{userID}")
    List<Pet> getPet(@PathVariable int userID) {
        return petService.getAllActivePet(userID);
    }


    @PutMapping("/update/{petid}")
    ResponseEntity<ResponseObject> updatePet(
            @PathVariable int petid,
            @RequestParam("request") String requestJson,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PetUpdateRequest request = objectMapper.readValue(requestJson, PetUpdateRequest.class);
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

    @DeleteMapping("/deletePet/{petId}")
    ResponseEntity<ResponseObject> deletePet(@PathVariable int petId) {
        String message = petService.deletePetByID(petId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", message, "")
        );
    }


    @PutMapping("/update-Vaccination")
    ResponseEntity<ResponseObject> updateVaccination(@RequestBody PetUpdateVacionationRequest request) {
        petService.updateVaccination(request.getPetId(), request.getVaccination());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Vaccination updated successfully"));
    }
}
