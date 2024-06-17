package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    User findUserByUserId(int id);
    User findByEmail(String email);
    List<User> findAllByRole(String role);
}
