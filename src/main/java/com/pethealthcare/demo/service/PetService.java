package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.dto.request.PetUpdateRequest;
import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.mapper.PetMapper;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    private PetMapper petMapper;


    public List<Pet> getAllPet() {
        return petRepository.findAll();
    }

    public Pet createPet(PetCreateRequest request) {
        boolean exist = petRepository.existsByPetname(request.getPetname());
        if (!exist) {
            Pet newPet = petMapper.toPet(request);
            newPet.setUserID(request.getUserID());
            newPet.setPetname(request.getPetname());
            newPet.setPetage(request.getPetage());
            newPet.setPetgender(request.getPetgender());
            newPet.setPettype(request.getPettype());
            newPet.setVaccination(request.getVaccination());
            return petRepository.save(newPet);
        }
        return null;
    }

    public Pet updatePet(int id, PetUpdateRequest request) {
        // Find user by id
        Optional<Pet> optionalPet = petRepository.findById(id);
        if (optionalPet.isPresent()) {
            Pet pet = optionalPet.get();

            // Update fields
            if (request.getPetname() != null && !request.getPetname().equals(pet.getPetname()) && !request.getPetname().isEmpty()) {
                pet.setPetname(request.getPetname());
            }
            if (request.getPetgender() != null && !request.getPetgender().equals(pet.getPetgender()) && !request.getPetgender().isEmpty()) {
                pet.setPetgender(request.getPetgender());
            }
            if(request.getPetage() > 0  && request.getPetage() ==(pet.getPetage())) {
                pet.setPetage(request.getPetage());
            }
            if(request.getPettype() != null && !request.getPettype().equals(pet.getPettype()) && !request.getPettype().isEmpty()) {
                pet.setPettype(request.getPettype());
            }
            if(request.getVaccination() != null && !request.getVaccination().equals(pet.getVaccination()) && !request.getVaccination().isEmpty()) {
                pet.setVaccination(request.getVaccination());
            }

            // Save updated user
            petRepository.save(pet);
            return pet;
    } else {
            return null;
        }
    }
}
