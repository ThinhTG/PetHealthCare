package com.pethealthcare.demo.service;


import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.mapper.UserMapper;
import com.pethealthcare.demo.model.ResponseObject;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.UserRepository;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpService otpService;

    public User register(UserCreateRequest request) {
        boolean exist = userRepository.existsByEmail(request.getEmail());
        if (!exist) {
            User newUser = userMapper.toUser(request);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRole("Customer");
            newUser.setStatus("Active");
            return userRepository.save(newUser);
        }
        return null;
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String otp = otpService.generateOtp(email);
            emailService.sendOtpMessage(email, "Reset Password", "Your otp is: " + otp);
            return "Please check your email address to get OTP";
        }
        return "Invalid Email Address";
    }

    public String checkOtp(String email, String otp) {
        boolean validOtp = otpService.validateOtp(email, otp);
        if (validOtp) {
            otpService.clearOtp(email);
            return "Enter your new password";
        }
        return "Invalid OTP";
    }

    public String resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "Reset password successfully";
    }
}
