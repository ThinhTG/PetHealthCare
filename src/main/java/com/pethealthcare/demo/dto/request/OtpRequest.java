package com.pethealthcare.demo.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    private String email;
    private String otp;
}
