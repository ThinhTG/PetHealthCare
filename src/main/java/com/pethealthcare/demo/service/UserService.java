package com.pethealthcare.demo.service;


import com.pethealthcare.demo.dto.request.AccCreateRequest;
import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.mapper.UserMapper;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpService otpService;

    public User createUser(UserCreateRequest request) {
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

    public User createAcc(AccCreateRequest request) {
        boolean exist = userRepository.existsByEmail(request.getEmail());
        if (!exist) {
            User newUser = userMapper.toUser(request);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setRole(request.getRole());
            newUser.setStatus("Active");
            newUser.setImageUrl(request.getImageUrl());

            return userRepository.save(newUser);
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(int userid, UserUpdateRequest request) {
        // Find user by id
        Optional<User> optionalUser = userRepository.findById(userid);
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
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
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

    public User getAccountById(int id) {
        return userRepository.findUserByUserId(id);
    }

    // Thêm phương thức để lấy tất cả người dùng có vai trò "Veterinarian"
    public List<User> getAllUsersByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    public List<User> getAllVeterinarians() {
        return getAllUsersByRole("Veterinarian");
    }


    public User updateUserRole(int userID, String newrole) {
        // Find user by id
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(newrole != null && !newrole.isEmpty()) {
                user.setRole(newrole);
            }
            // Save updated user
            userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    public boolean deleteUser(int userID) {
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus("Deleted");
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}
