package com.pethealthcare.controller;

import com.pethealthcare.dto.request.ServiceCreateRequest;
import com.pethealthcare.model.ResponseObject;
import com.pethealthcare.model.Services;
import com.pethealthcare.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Service")
public class ServiceController {
    @Autowired
    ServiceService service;

    @GetMapping("/getAll")
    List<Services> getAllService() {
        return service.getAllServices();
    }

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createUser(@RequestBody ServiceCreateRequest request) {
        Services createdService = service.createService(request);
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
    ResponseEntity<ResponseObject> updateUser(@PathVariable int id, @RequestBody ServiceCreateRequest request) {
        Services updateService = service.updateService(id, request);
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

    @DeleteMapping("/delete/{serviceID}")
    ResponseEntity<ResponseObject> deletePet(@PathVariable int serviceID) {


            Services foundService = service.getServiceById(serviceID);
            if (foundService != null) {
                service.deleteService(serviceID);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Delete service Successfully", "")
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "service not found", "")
            );
    }

}
