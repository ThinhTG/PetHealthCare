package com.pethealthcare.demo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalHistoryCreateRequest {
    private String VeterinaryName;
    private String TreatmentResult;
    private Date DateMedical;
}
