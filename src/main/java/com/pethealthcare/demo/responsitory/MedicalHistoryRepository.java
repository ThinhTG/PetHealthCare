package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.MedicalHistory;
import com.pethealthcare.demo.model.Pet;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {
    List<MedicalHistory> findMedicalHistoriesByPet(MedicalHistory medicalHistory);
    boolean existsByMedicalHistoryId(int medicalHisoryId);
    void deleteMedicalHistoryByMedicalHistoryId(int medicalHistoryId);
    MedicalHistory findMedicalHistoryByMedicalHistoryId(int medicalHistoryId);

}
