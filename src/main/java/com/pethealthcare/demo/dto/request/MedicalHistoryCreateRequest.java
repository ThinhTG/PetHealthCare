package com.pethealthcare.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryCreateRequest {
    private int petId;

    private int bookingDetailId;

    private String veterinaryName;

    private LocalDate dateMedicalHistory;

    private String diseaseName;

    private String treatmentMethod;

    private String note;

    private String reminders;

}
