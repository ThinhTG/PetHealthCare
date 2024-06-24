package com.pethealthcare.dto.request;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailCreateRequest {
    private int petId;
    private int veterinarianId;
    private int serviceId;
    private boolean needCage;
    private Date date;
    private int slotId;
}
