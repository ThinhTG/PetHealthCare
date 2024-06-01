package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.User;

import javax.security.auth.login.AccountException;
import java.util.Optional;

public interface IUserService {
    public User login(String username, String password)throws AccountException;
}