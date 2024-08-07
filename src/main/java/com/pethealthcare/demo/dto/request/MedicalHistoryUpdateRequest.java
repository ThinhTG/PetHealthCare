package com.pethealthcare.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryUpdateRequest {
    private String veterinaryName;

    private String treatmentResult;

    private LocalDate dateMedical;

}
