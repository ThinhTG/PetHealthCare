package com.pethealthcare.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private int userId;
    private String name;
    private String email;
    private String oldPassword;
    private String newPassword;
    private String phone;
    private String address;
}
