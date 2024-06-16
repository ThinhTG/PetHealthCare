package com.pethealthcare.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSlotCreateRequest {
    private int userId;
    private int slotId;
    private Date date;
    private boolean status;
}
