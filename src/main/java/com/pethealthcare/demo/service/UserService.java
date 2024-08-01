package com.pethealthcare.demo.service;


import com.pethealthcare.demo.dto.request.AccCreateRequest;
import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.enums.BookingStatus;
import com.pethealthcare.demo.mapper.UserMapper;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.repository.BookingDetailRepository;
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
import java.util.Arrays;
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
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    public List<User> getAll(){
        return userRepository.findAll();
    }

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

    public String updateUser(int userId, UserUpdateRequest request, MultipartFile file) throws IOException {
        User user = userRepository.findUserByUserId(userId);
        List<Booking> bookings = bookingRepository.
                findBookingByUser_UserIdAndStatusIn(userId,
                        Arrays.asList(BookingStatus.PENDING, BookingStatus.PAID));
        if (bookings != null) {
            return "User is existing in booking";
        }
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
        if (user == null) {
            return false;
        }

        List<Booking> bookings = bookingRepository.
                findBookingByUser_UserIdAndStatusIn(userID,
                        Arrays.asList(BookingStatus.PENDING, BookingStatus.PAID));
        if (bookings != null && !bookings.isEmpty()) {
            return false;
        }

        user.setStatus(false);
        userRepository.save(user);
        return true;
    }


    public User cancelDeleteUser(int userID) {
        User user = userRepository.findUserByUserId(userID);
        if (user == null) {
            return null;
        }
        user.setStatus(true);
        userRepository.save(user);
        return user;
    }

}
