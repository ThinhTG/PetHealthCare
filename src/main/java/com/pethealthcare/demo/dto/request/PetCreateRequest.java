package com.pethealthcare.demo.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetCreateRequest {
    private int userID;
    private String petname;
    private int petage;
    private String petgender;
    private String pettype;
    private String vaccination;
}
