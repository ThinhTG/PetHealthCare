package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.model.Account;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.responsitory.AccountResponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Account")
public class AccountController {
    @Autowired
   private AccountResponsitory responsitory;

@GetMapping("/all")
List<Account> getAll() {
    return responsitory.findAll();
}




}
