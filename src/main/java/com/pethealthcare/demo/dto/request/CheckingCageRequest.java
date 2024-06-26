package com.pethealthcare.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingCageRequest {
    private int cageId;
    private int bookingDetailId;

}
