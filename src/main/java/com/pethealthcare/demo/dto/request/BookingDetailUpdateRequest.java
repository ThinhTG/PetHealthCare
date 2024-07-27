package com.pethealthcare.demo.dto.request;

import com.pethealthcare.demo.model.ServiceSlot;
import com.pethealthcare.demo.model.Services;
import com.pethealthcare.demo.model.Slot;
import com.pethealthcare.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailUpdateRequest {
    private ServiceSlot serviceSlot;
}
