package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.MedicalHistoryCreateRequest;
import com.pethealthcare.demo.dto.request.MedicalHistoryUpdateRequest;
import com.pethealthcare.demo.model.MedicalHistory;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.service.MedicalHistoryService;
import com.pethealthcare.demo.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical-history")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PetService petService;

    @GetMapping("/getAll")
    List<MedicalHistory> getAllMedicalHistory() {
        return medicalHistoryService.medicalHistories();
    }

    @PostMapping("/create/{id}")
    ResponseEntity<ResponseObject> createMedicalHistory(@PathVariable int id, @RequestBody MedicalHistoryCreateRequest request) {
        MedicalHistory medicalHistory = medicalHistoryService.createMedicalHistory(id, request);
        if (medicalHistory != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("ok", "Medical history successfully", medicalHistory)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Try again", "")
            );
        }
    }

    @PutMapping("/update/{medicalHistoryId}")
    ResponseEntity<ResponseObject> updateMedicalHistory(@PathVariable int medicalHistoryId, @RequestBody MedicalHistoryUpdateRequest request) {
        MedicalHistory updateMedicalHistory = medicalHistoryService.updateMedicalHistory(medicalHistoryId, request);
        if (updateMedicalHistory != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "User updated successfully", updateMedicalHistory)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "User not found", "")
            );
        }
    }

    @GetMapping("/getMedicalHistoryByPetStayCage")
    List<MedicalHistory> getMedicalHistory() {
        return medicalHistoryService.getMedicalHistoryByPetStayCage();
    }

    @GetMapping("/getAll/{petID}")
    List<MedicalHistory> getMedicalHistoriesByPetID(@PathVariable int petID) {
        return medicalHistoryService.getMedicalHistoriesByPetId(petID);
    }

    @DeleteMapping("/deleteMedicalHistory/{medicalhistoryid}/{petid}")
    ResponseEntity<ResponseObject> deleteMedicalHistory(@PathVariable int medicalhistoryid,@PathVariable int petid) {
        Pet pet = petService.findPetByPetID(petid);
        if (pet != null) {
            boolean existsMedicalHistory = medicalHistoryService.findMedicalHistoryByID(medicalhistoryid);
            if (existsMedicalHistory) {
                medicalHistoryService.deleteMedicalHistory(medicalhistoryid);
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

    @PutMapping("/updateStatus")
    ResponseEntity<String> updateStatus(@RequestParam int id, @RequestParam String status){
        medicalHistoryService.updateStatus(id, status);
        return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
    }
}
