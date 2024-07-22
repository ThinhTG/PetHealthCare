package com.pethealthcare.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccCreateRequest {
    private String name;
    private String email;
    private String password;
    private BigDecimal phone;
    private String address;
    private String role;
    private String imageUrl;
}
