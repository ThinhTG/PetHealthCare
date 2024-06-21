package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.MedicalHistoryCreateRequest;
import com.pethealthcare.demo.dto.request.MedicalHistoryUpdateRequest;
import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.mapper.MedicalHistoryMapper;
import com.pethealthcare.demo.model.MedicalHistory;
import com.pethealthcare.demo.model.Pet;
import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.responsitory.MedicalHistoryRepository;
import com.pethealthcare.demo.responsitory.PetRepository;
import com.pethealthcare.demo.responsitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalHistoryService  {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MedicalHistoryMapper medicalHistoryMapper;

    public List<MedicalHistory> medicalHistories() {
        return medicalHistoryRepository.findAll();
    }

    public MedicalHistory createMedicalHistory(int id, MedicalHistoryCreateRequest request) {
        Pet pet = petRepository.findPetByPetId(id);

        MedicalHistory newMedicalHistory = medicalHistoryMapper.toMedicalHistory(request);
        newMedicalHistory.setPet(pet);
        return medicalHistoryRepository.save(newMedicalHistory);

    }

    public boolean deleteMedicalHistory(int id) {
        if (medicalHistoryRepository.existsByMedicalHistoryId(id)) {
            medicalHistoryRepository.deleteMedicalHistoryByMedicalHistoryId(id);
            return true;
        }
        return false;
    }

    public MedicalHistory getMedicalHistory(int id) {
        return medicalHistoryRepository.findMedicalHistoryByMedicalHistoryId(id);
    }

    public List<MedicalHistory> getMedicalHistoriesByPetId(int petId){
        MedicalHistory medicalHistory = new MedicalHistory();
        Pet pet = new Pet();
        pet.setPetId(petId);
        return medicalHistoryRepository.findMedicalHistoriesByPet(pet);
    }

    public MedicalHistory updateMedicalHistory(int medicalHistoryId, MedicalHistoryUpdateRequest request) {

        Optional<MedicalHistory> optionalMedicalHistory = medicalHistoryRepository.findById(medicalHistoryId);
        if (optionalMedicalHistory.isPresent()) {
            MedicalHistory medicalHistory = optionalMedicalHistory.get();

            // Update fields
            if (request.getDateMedical() != null && !request.getDateMedical().equals(medicalHistory.getDateMedical())) {
                medicalHistory.setDateMedical(request.getDateMedical());
            }
            if (request.getTreatmentResult() != null && !request.getTreatmentResult().equals(medicalHistory.getTreatmentResult()) && !request.getTreatmentResult().isEmpty()) {
                medicalHistory.setTreatmentResult(request.getTreatmentResult());
            }
            if (request.getVeterinaryName() != null && !request.getVeterinaryName().equals(medicalHistory.getVeterinaryName()) && !request.getVeterinaryName().isEmpty()) {
                medicalHistory.setVeterinaryName(request.getVeterinaryName());
            }

            medicalHistoryRepository.save(medicalHistory);
            return medicalHistory;
        } else {
            return null;
        }
    }

    public boolean findMedicalHistoryByID(int id) {
        return medicalHistoryRepository.existsByMedicalHistoryId(id);
    }

}
