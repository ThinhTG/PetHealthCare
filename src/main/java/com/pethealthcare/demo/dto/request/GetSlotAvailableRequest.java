package com.pethealthcare.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSlotAvailableRequest {
    private int userId;
    private LocalDate date;
}
