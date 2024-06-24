package com.pethealthcare.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private String name;
    private String email;
    private String password;
    private BigDecimal phone;
    private String address;
}
