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
    private UserRepository userRepository;

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

    @Override
    public User getAccountById(int id) {
        return UserRepository.findUserByUserID(id);
    }

}
