package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MedicalHistory")
@Entity
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medicalHistoryId;

    @Column (name = "VeterinaryName",columnDefinition = "nvarchar(50)")
    private String veterinaryName;

    @Column (name = "TreatmentResult",columnDefinition = "nvarchar(MAX)")
    private String treatmentResult;

    @Column (name = "DateMedical", columnDefinition = "date")
    private LocalDate dateMedicalHistory;

    @Column
    private String status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "petId")
    private Pet pet;
}
