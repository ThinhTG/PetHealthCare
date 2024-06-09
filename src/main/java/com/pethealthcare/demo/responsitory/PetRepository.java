package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    boolean existsByPetname(String petname);
    boolean existsByPetid(int petid);
   List<Pet> findPetsByUserID(int userID);


    void deleteById(int id);
}
