package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.ServiceCreateRequest;
import com.pethealthcare.demo.enums.ServiceStatus;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/Service")
public class ServiceController {
    @Autowired
    private ServiceService service;

    @GetMapping("/getAll")
    List<Services> getAllService() {
        return service.getAllServices();
    }

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createService(@RequestBody ServiceCreateRequest request, @RequestParam MultipartFile file) throws IOException {
        Services createdService = service.createService(request, file);
        if (createdService != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("ok", "Service created successfully", createdService)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Service already created", "")
            );
        }
    }

    @PutMapping("/update/{id}")
    ResponseEntity<ResponseObject> updateService(@PathVariable int id, @RequestBody ServiceCreateRequest request, @RequestParam MultipartFile file) throws IOException {
        Services updateService = service.updateService(id, request, file);
        if (updateService != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Service updated successfully", updateService)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Service not found", "")
            );
        }
    }
}
