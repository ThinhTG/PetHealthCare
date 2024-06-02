package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.AuthenticationRequest;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(AuthenticationRequest request) {
        boolean exists = userRepository.existsByEmail(request.getEmail());
        if (exists) {
            User user = userRepository.findByEmail(request.getEmail());
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            return passwordEncoder.matches(request.getPassword(), user.getPassword());
        }
        return false;
    }
}
