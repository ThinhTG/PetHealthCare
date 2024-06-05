package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    boolean existsByPetname(String petname);
}
