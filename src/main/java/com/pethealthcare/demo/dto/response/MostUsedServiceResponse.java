package com.pethealthcare.demo.dto.response;

import com.pethealthcare.demo.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostUsedServiceResponse {
    private int count;
    private Services services;
}
