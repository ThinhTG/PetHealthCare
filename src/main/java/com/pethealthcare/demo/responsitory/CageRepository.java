package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Cage;
import com.pethealthcare.demo.model.CageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CageRepository extends JpaRepository<Cage, Integer> {

    Cage findById(int cageId);
    List<Cage> findCageByCageType(CageType type);



}
