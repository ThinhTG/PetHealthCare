package com.pethealthcare.responsitory;

import com.pethealthcare.model.Pet;
import com.pethealthcare.model.User;
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
}
