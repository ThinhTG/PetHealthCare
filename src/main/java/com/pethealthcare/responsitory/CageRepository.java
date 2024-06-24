package com.pethealthcare.responsitory;

import com.pethealthcare.model.Cage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CageRepository extends JpaRepository<Cage, Integer> {

    Cage findById(int cageId);



}
