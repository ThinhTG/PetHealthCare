package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
