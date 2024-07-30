package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.GetSlotAvailableRequest;
import com.pethealthcare.demo.dto.request.GetVetAvailableRequest;
import com.pethealthcare.demo.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.demo.enums.ServiceSlotStatus;
import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.repository.ServiceSlotRepository;
import com.pethealthcare.demo.repository.UserRepository;
import com.pethealthcare.demo.service.ServiceSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sev-slot")
public class ServiceSlotController {
    @Autowired
    private ServiceSlotService serviceSlotService;

    @PostMapping("/add")
    ResponseEntity<String> createServiceSlot(@RequestBody List<ServiceSlotCreateRequest> request) {
        return new ResponseEntity<>(serviceSlotService.addServiceSlots(request), HttpStatus.CREATED);
    }

    @PostMapping ("/slot-available")
    List<ServiceSlot> getSlotAvailable(@RequestBody GetSlotAvailableRequest request) {
        return serviceSlotService.getSlotAvailable(request);
    }

    @PostMapping ("/vet-available")
    List<ServiceSlot> getVetAvailable(@RequestBody GetVetAvailableRequest request) {
        return serviceSlotService.getVetAvailable(request);
    }

    @PostMapping ("/slot-not-create")
    List<ServiceSlot> getSlotNotCreate(@RequestBody GetSlotAvailableRequest request) {
        return serviceSlotService.getSlotNotCreate(request);
    }

    @PutMapping("/vetCancelDate")
    List<ServiceSlot> serviceSlotCancelDate(@RequestParam LocalDate date, @RequestParam int vetId) {
        return serviceSlotService.cancelServiceSlot(date, vetId);
    }

    @GetMapping("/getByVet")
    List<ServiceSlot> getByVet(@RequestParam int vetId, @RequestParam LocalDate date) {
        return serviceSlotService.getSlotByDateAndVet(vetId, date);
    }
}
