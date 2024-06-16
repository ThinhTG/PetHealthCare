package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.GetSlotAvailableRequest;
import com.pethealthcare.demo.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.service.ServiceSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sev-slot")
public class ServiceSlotController {
    @Autowired
    private ServiceSlotService serviceSlotService;

    @PostMapping("/add")
    ResponseEntity<String> createServiceSlot(ServiceSlotCreateRequest request) {
        return new ResponseEntity<>(serviceSlotService.addServiceSlot(request), HttpStatus.CREATED);
    }

    @GetMapping("/slot-available")
    List<ServiceSlot> getSlotAvailable(GetSlotAvailableRequest request) {
        return serviceSlotService.getSlotAvailable(request);
    }
}
