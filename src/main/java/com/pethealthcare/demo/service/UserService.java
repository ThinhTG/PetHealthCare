package com.pethealthcare.demo.service;



import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.UserRepository;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.Optional;

    @Service
    public class UserService implements IUserService{
        @Autowired
        private UserRepository responsitory;




       public List<User> getAllUser() {
            return responsitory.findAll();
        }


        @Override
        public User login(String email, String password) throws AccountException {
                Optional<User> account = responsitory.findByEmail(email);
                if (!account.isPresent()) {
                    throw new AccountException("User is not found");
                }
                if (account.get().getPassword().equals(password)) {
                    return account.get();
                } else {
                    throw new AccountException("Password is incorrect");
                }
            }
    }
