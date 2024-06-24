package com.pethealthcare.controller;

import com.pethealthcare.dto.request.GetSlotAvailableRequest;
import com.pethealthcare.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.model.ServiceSlot;
import com.pethealthcare.service.ServiceSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/slot-available")
    List<ServiceSlot> getSlotAvailable(@RequestBody GetSlotAvailableRequest request) {
        return serviceSlotService.getSlotAvailable(request);
    }
}
