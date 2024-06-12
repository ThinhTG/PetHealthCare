package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Integer> {
    boolean existsByName(String name);
}
