package com.pethealthcare.demo.dto.response;

import com.pethealthcare.demo.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostUsedServiceResponse {
    private int count;
    private List<Services> services;
}
