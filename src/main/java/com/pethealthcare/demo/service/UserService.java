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
public class UserService implements IUserService {
    @Autowired
    private UserRepository UserRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpService otpService;

    @Override
    public User createUser(UserCreateRequest request) {
        boolean exist = UserRepository.existsByEmail(request.getEmail());
        if (!exist) {
            User newUser = userMapper.toUser(request);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRole("Admin");
            newUser.setStatus("Active");

            return UserRepository.save(newUser);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }

    @Override
    public User updateUser(int userid, UserUpdateRequest request) {
        // Find user by id
        Optional<User> optionalUser = UserRepository.findById(userid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Update fields
            if (request.getName() != null && !request.getName().equals(user.getName()) && !request.getName().isEmpty()) {
                user.setName(request.getName());
            }
            if (request.getEmail() != null && !request.getEmail().equals(user.getEmail()) && !request.getEmail().isEmpty()) {
                user.setEmail(request.getEmail());
            }
            if (request.getPassword() != null && !request.getPassword().equals(user.getPassword()) && !request.getPassword().isEmpty()) {
                // Encode password as JWT
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            if(request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
                user.setPhone(request.getPhone());
            }
            if(request.getAddress() != null && !request.getAddress().equals(user.getAddress())) {
                user.setAddress(request.getAddress());
            }

            // Save updated user
            UserRepository.save(user);
            return user;
        } else {
            return null;
        }
    }



    public User register(UserCreateRequest request) {
        boolean exist = UserRepository.existsByEmail(request.getEmail());
        if (!exist) {
            User newUser = userMapper.toUser(request);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRole("Customer");
            newUser.setStatus("Active");
            return UserRepository.save(newUser);
        }
        return null;
    }

    public String forgotPassword(String email) {
        User user = UserRepository.findByEmail(email);
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
        User user = UserRepository.findByEmail(email);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(password));
        UserRepository.save(user);
        return "Reset password successfully";
    }

    @Override
    public User getAccountById(int id) {
        return UserRepository.findUserByUserID(id);
    }

}
