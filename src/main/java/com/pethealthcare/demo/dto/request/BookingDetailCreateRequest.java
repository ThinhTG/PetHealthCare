package com.pethealthcare.demo.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailCreateRequest {
    private int petId;
    private int veterinarianId;
    private int serviceId;
    private boolean needCage;
    private LocalDate date;
    private int slotId;
}
