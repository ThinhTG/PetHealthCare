package com.pethealthcare.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryCreateRequest {
    @JsonProperty("VeterinaryName")
    private String veterinaryName;

    @JsonProperty("TreatmentResult")
    private String treatmentResult;

    @JsonProperty("DateMedical")
    private Date dateMedical;
}
