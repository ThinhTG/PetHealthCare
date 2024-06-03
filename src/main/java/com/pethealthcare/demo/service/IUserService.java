package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.model.User;

import java.util.List;

public interface IUserService {
    public User createUser(UserCreateRequest request);

    public List<User> getAllUsers();

    public User updateUser(int userid, UserUpdateRequest request);
}