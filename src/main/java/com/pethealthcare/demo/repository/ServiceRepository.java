package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Integer> {
    boolean existsByName(String name);

    Services findServicesByServiceId(int id);

    Services findByServiceId(int id);

    List<Services> findAllByStatus(boolean status);
}
