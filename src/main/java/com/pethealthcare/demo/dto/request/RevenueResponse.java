package com.pethealthcare.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RevenueResponse {
    private int month;
    private double revenue;
}
