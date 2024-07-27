package com.pethealthcare.demo.dto.request;

import com.pethealthcare.demo.enums.BookingDetailStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailStatusUpdateRequest {
    private BookingDetailStatus status;
}