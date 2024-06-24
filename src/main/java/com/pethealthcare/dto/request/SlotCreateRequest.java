package com.pethealthcare.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotCreateRequest {
    private Time startTime;
    private Time endTime;
}
