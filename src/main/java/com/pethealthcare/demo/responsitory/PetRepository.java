package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    boolean existsByPetname(String petname);

   List<Pet> findPetsByUserID(int userID);
}
