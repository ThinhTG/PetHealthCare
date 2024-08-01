package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.MedicalHistory;
import com.pethealthcare.demo.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {
    List<MedicalHistory> findMedicalHistoriesByPet(Pet medicalHistory);

    boolean existsByMedicalHistoryId(int medicalHistoryId);

    void deleteMedicalHistoryByMedicalHistoryId(int medicalHistoryId);

    MedicalHistory findMedicalHistoryByBookingDetail_BookingDetailId(int bookingDetailId);
}
