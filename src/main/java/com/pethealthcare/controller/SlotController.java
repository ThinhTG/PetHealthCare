package com.pethealthcare.controller;

import com.pethealthcare.dto.request.SlotCreateRequest;
import com.pethealthcare.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/slot")
public class SlotController {
    @Autowired
    private SlotService slotService;

    @PostMapping("/add")
    ResponseEntity<String> addSlot(@RequestBody SlotCreateRequest request) {
        return new ResponseEntity<>(slotService.addSlot(request), HttpStatus.OK);
    }

}
