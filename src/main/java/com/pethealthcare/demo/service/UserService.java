package com.pethealthcare.demo.service;


import com.pethealthcare.demo.dto.request.AccCreateRequest;
import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.mapper.UserMapper;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.repository.BookingRepository;
import com.pethealthcare.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    @Autowired
    private WalletService walletService;
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    @Autowired
    private BookingRepository bookingRepository;

    public User createUser(UserCreateRequest request) {
        User exist = userRepository.findUserByEmail(request.getEmail());
        if (exist != null && exist.isStatus()) {
            return null;
        }
        User newUser = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("Customer");
        newUser.setStatus(true);
        User user = userRepository.save(newUser);
        walletService.createWallet(user.getUserId());
        return user;
    }

    public User createAcc(AccCreateRequest request, MultipartFile file) throws IOException {
        User exist = userRepository.findUserByEmail(request.getEmail());
        if (exist != null && exist.isStatus()) {
            return null;
        }
        User newUser = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        newUser.setStatus(true);

        if (file != null && !file.isEmpty()) {
            String fileName = firebaseStorageService.uploadFile(file);
            newUser.setImageUrl(fileName);
        }

        User user = userRepository.save(newUser);
        walletService.createWallet(user.getUserId());
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByStatus(true);
    }

    public String updateUser(UserUpdateRequest request, MultipartFile file) throws IOException {
        User user = userRepository.findUserByUserId(request.getUserId());
        Booking booking = bookingRepository.findBookingByUser_UserIdAndStatus(request.getUserId(), "PENDING");
        if (booking == null) {
            booking = bookingRepository.findBookingByUser_UserIdAndStatus(request.getUserId(), "CONFIRMED");
        }
        if (booking != null) {
            return "User is existing in booking";
        }
            if (request.getName() != null && !request.getName().equals(user.getName()) && !request.getName().isEmpty()) {
                user.setName(request.getName());
            }
            if (request.getEmail() != null && !request.getEmail().equals(user.getEmail()) && !request.getEmail().isEmpty()) {
                user.setEmail(request.getEmail());
            }
        if (request.getOldPassword() != null && !request.getOldPassword().isEmpty()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return "Old password is incorrect";
            }
        }
        if (request.getNewPassword() != null && !request.getNewPassword().equals(user.getPassword()) && !request.getNewPassword().isEmpty()) {
                // Encode password as JWT
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }
            if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
                user.setPhone(request.getPhone());
            }
            if (request.getAddress() != null && !request.getAddress().equals(user.getAddress())) {
                user.setAddress(request.getAddress());
            }

            if (file != null && !file.isEmpty()) {
                String fileName = firebaseStorageService.uploadFile(file);
                user.setImageUrl(fileName);
            }
            // Save updated user
            userRepository.save(user);
        return "Update user successfully";
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

    public List<User> getAllVeterinariansActive() {
        List<User> users = getAllUsersByRole("Veterinarian");
        List<User> activeUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isStatus()) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }


    public User updateUserRole(int userID, String newrole) {
        // Find user by id
        Optional<User> optionalUser = userRepository.findById(userID);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (newrole != null && !newrole.isEmpty()) {
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
        User user = userRepository.findUserByUserId(userID);
        Booking booking = bookingRepository.findBookingByUser_UserIdAndStatus(userID, "PENDING");
        if (booking == null) {
            booking = bookingRepository.findBookingByUser_UserIdAndStatus(userID, "CONFIRMED");
        }
        if (booking != null) {
            return false;
        }
        user.setStatus(false);
        userRepository.save(user);
        return true;
    }
}
