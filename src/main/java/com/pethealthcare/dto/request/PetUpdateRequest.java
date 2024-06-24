package com.pethealthcare.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetUpdateRequest {
    private String petName;
    private int petAge;
    private String petGender;
    private String petType;
    private String vaccination;
}
