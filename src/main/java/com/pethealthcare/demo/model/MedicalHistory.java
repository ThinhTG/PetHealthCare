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

    @Column
    private String veterinaryName;

    @Column
    private LocalDate dateMedicalHistory;

    @Column
    private String diseaseName;

    @Column
    private String treatmentMethod;

    @Column
    private String note;

    @Column
    private String reminders;

    @Column
    private String vaccine;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "petId")
    private Pet pet;
}
