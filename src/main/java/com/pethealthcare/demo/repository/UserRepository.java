package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    User findUserByUserId(int id);
    User findByEmail(String email);
    List<User> findAllByRole(String role);
    User findUserByPhone(String phone);
}
