package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.dto.request.PetUpdateRequest;
import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.mapper.PetMapper;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.repository.BookingDetailRepository;
import com.pethealthcare.demo.repository.PetRepository;
import com.pethealthcare.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetMapper petMapper;
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    public List<Pet> getAllPet() {
        return petRepository.findAll();
    }

    public List<Pet> getAllActivePet(int userID) {
        User user = userRepository.findUserByUserId(userID);
        List<Pet> pets = petRepository.findPetsByUser(user);
        List<Pet> petss = new ArrayList<>();
        for (Pet pet : pets) {

            if (!pet.isDeleted()) {
                petss.add(pet);
            }

        }
        return petss;
    }

    public Pet createPet(int id, PetCreateRequest request, MultipartFile file) throws IOException {
        User user = userRepository.findUserByUserId(id);

        Pet pet = petRepository.findPetByUser_UserIdAndAndPetName(id, request.getPetName());
        if (pet != null && !pet.isDeleted()) {
            return null;
        }
        Pet newPet = petMapper.toPet(request);
        newPet.setUser(user);
        newPet.setStayCage(false);

        if (file != null && !file.isEmpty()) {
            String fileName = firebaseStorageService.uploadFile(file);
            newPet.setImageUrl(fileName);
        }

        newPet.setDeleted(false);
        return petRepository.save(newPet);
    }

    public Pet updatePet(int petId, PetUpdateRequest request, MultipartFile file) throws IOException {
        Pet deletePet = petRepository.findPetByPetId(petId);
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByPet_PetIdAndStatus(petId, BookingDetailStatus.WAITING);
        if (bookingDetail != null) {
            throw new RuntimeException("Pet is existing in booking");
        }
        if (deletePet != null) {

            // Update fields
            if (request.getPetName() != null && !request.getPetName().equals(deletePet.getPetName()) && !request.getPetName().isEmpty()) {
                deletePet.setPetName(request.getPetName());
            }
            if (request.getPetGender() != null && !request.getPetGender().equals(deletePet.getPetGender()) && !request.getPetGender().isEmpty()) {
                deletePet.setPetGender(request.getPetGender());
            }
            if (request.getPetAge() > 0 && request.getPetAge() != (deletePet.getPetAge())) {
                deletePet.setPetAge(request.getPetAge());
            }
            if (request.getPetType() != null && !request.getPetType().equals(deletePet.getPetType()) && !request.getPetType().isEmpty()) {
                deletePet.setPetType(request.getPetType());
            }
            if (request.getVaccination() != null && !request.getVaccination().equals(deletePet.getVaccination()) && !request.getVaccination().isEmpty()) {
                deletePet.setVaccination(request.getVaccination());
            }
            if (file != null && !file.isEmpty()) {
                String fileName = firebaseStorageService.uploadFile(file);
                deletePet.setImageUrl(fileName);
            }
            petRepository.save(deletePet);
            return deletePet;
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

    public String deletePetByID(int id) {
        Pet deletePet = petRepository.findPetByPetId(id);
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByPet_PetIdAndStatus(id, BookingDetailStatus.WAITING);
        if (bookingDetail != null) {
            return "Pet is existing in booking";
        }
        deletePet.setDeleted(true);
        petRepository.save(deletePet);
        return "Delete pet successfully";
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
