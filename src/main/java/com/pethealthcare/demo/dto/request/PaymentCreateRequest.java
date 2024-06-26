package com.pethealthcare.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateRequest {
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private double deposit;
    private double total;
    private int bookingId;
}
