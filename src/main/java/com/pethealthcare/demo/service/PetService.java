package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.dto.request.PetUpdateRequest;
import com.pethealthcare.demo.mapper.PetMapper;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.repository.PetRepository;
import com.pethealthcare.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    private PetMapper petMapper;

    @Autowired
    private UserRepository userRepository;

    public List<Pet> getAllPet() {
        return petRepository.findAll();
    }

    public Pet createPet(int id,PetCreateRequest request) {
        User user = userRepository.findUserByUserId(id);

        boolean exist = petRepository.existsByUserAndPetName(user, request.getPetName());
        if (!exist) {
            Pet newPet = petMapper.toPet(request);
            newPet.setUser(user);
            newPet.setStayCage(false);
            newPet.setDeleted(false);
            return petRepository.save(newPet);
        }
        return null;
    }

    public Pet updatePet(int userID,int petid, PetUpdateRequest request) {
        // Find user by id
        Optional<Pet> optionalPet = petRepository.findById(petid);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();

            // Update fields
            if (request.getPetName() != null && !request.getPetName().equals(pet.getPetName()) && !request.getPetName().isEmpty()) {
                pet.setPetName(request.getPetName());
            }
            if (request.getPetGender() != null && !request.getPetGender().equals(pet.getPetGender()) && !request.getPetGender().isEmpty()) {
                pet.setPetGender(request.getPetGender());
            }
            if (request.getPetAge() > 0 && request.getPetAge() != (pet.getPetAge())) {
                pet.setPetAge(request.getPetAge());
            }
            if (request.getPetType() != null && !request.getPetType().equals(pet.getPetType()) && !request.getPetType().isEmpty()) {
                pet.setPetType(request.getPetType());
            }
            if(request.getVaccination() != null && !request.getVaccination().equals(pet.getVaccination()) && !request.getVaccination().isEmpty()) {
                pet.setVaccination(request.getVaccination());
            }

            petRepository.save(pet);
            return pet;
    } else {
            return null;
        }
    }

    public List<Pet> getPetsByUserID(int userId) {
        User user = new User();
        user.setUserId(userId);
        return petRepository.findPetsByUser(user);
    }

    public boolean findPetByID(int id) {
        return petRepository.existsByPetId(id);
    }

    public Pet findPetByPetID(int id) {
        return petRepository.findPetByPetId(id);
    }

    public void deletePetByID(int id){
        Pet deletePet = petRepository.findPetByPetId(id);

        if (deletePet != null && !deletePet.isDeleted()) {
            deletePet.setDeleted(true);
            petRepository.save(deletePet);
        }
    }

    public void updateVaccination(int petId, String vaccination) {
        Pet pet = petRepository.findPetByPetId(petId);
        if (pet.getVaccination() == null || pet.getVaccination().isEmpty()) {
            pet.setVaccination(vaccination);
        } else {
            pet.setVaccination(pet.getVaccination() + ", " + vaccination);
        }
        petRepository.save(pet);
    }
}
