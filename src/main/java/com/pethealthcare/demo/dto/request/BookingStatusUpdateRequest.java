package com.pethealthcare.demo.dto.request;

import com.pethealthcare.demo.enums.BookingDetailStatus;
import com.pethealthcare.demo.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatusUpdateRequest {
    private BookingStatus status;
}
