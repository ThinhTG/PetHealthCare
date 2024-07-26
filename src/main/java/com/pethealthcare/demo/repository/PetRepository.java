package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    boolean existsByUserAndPetName(User user, String petName);

    boolean existsByPetId(int petId);

    List<Pet> findPetsByUser(User user);

    void deletePetByPetId(int petId);

    Pet findPetByPetId(int petId);

    List<Pet> getPetByStayCage(boolean stayCage);

    List<Pet> getPetByIsDeleted(boolean status, User user);
}
