package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.UserRepository;
import com.pethealthcare.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Account")
public class AccountController {
    @Autowired
    UserService userService;

    @GetMapping("/all")
    List<User> all() {
        return userService.getAllUser();
    }











}
