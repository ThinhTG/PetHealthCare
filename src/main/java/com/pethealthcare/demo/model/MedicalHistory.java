package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String VeterinaryName;

    @Column
    private String TreatmentResult;

    @Column
    private Date DateMedical;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "petId")
    private Pet pet;
}
