package com.pethealthcare.demo.dto.request;

import com.pethealthcare.demo.model.Services;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Provider;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostServiceResponse {
    private int serviceId;
    private long usageCount;
}
