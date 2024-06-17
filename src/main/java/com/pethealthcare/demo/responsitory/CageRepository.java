package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Cage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CageRepository extends JpaRepository<Cage, Integer> {

    Cage findById(int cageId);



}
