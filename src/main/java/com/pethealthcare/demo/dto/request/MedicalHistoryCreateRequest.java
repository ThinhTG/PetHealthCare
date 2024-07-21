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

    private String veterinaryName;


    private String treatmentResult;


    private Date dateMedical;
}
