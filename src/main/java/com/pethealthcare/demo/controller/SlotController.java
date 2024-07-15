package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.SlotCreateRequest;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slot")
public class SlotController {
    @Autowired
    private SlotService slotService;

    @PostMapping("/add")
    ResponseEntity<String> addSlot(@RequestBody SlotCreateRequest request) {
        return new ResponseEntity<>(slotService.addSlot(request), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<String> updateSlot(@PathVariable int id, @RequestBody SlotCreateRequest request) {
        return new ResponseEntity<>(slotService.updateSlot(id, request), HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<List<Slot>> getAllSlots() {
        return new ResponseEntity<>(slotService.getAllSlots(), HttpStatus.OK);
    }
}
