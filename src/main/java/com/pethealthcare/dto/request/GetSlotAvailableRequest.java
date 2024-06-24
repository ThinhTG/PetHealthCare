package com.pethealthcare.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSlotAvailableRequest {
    private int userId;
    private Date date;
}
