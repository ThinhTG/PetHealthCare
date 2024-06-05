package com.pethealthcare.demo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetUpdateRequest {
    private String petname;
    private int petage;
    private String petgender;
    private String pettype;
    private String vaccination;
}
