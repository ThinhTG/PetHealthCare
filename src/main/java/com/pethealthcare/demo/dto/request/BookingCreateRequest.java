package com.pethealthcare.demo.dto.request;

import com.pethealthcare.demo.model.User;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {
    private int customerId;
    private Date date;
    private String status;
    private double totalPrice;
    private List<BookingDetailCreateRequest>  bookingDetails;
}
