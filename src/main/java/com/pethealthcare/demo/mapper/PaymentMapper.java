package com.pethealthcare.demo.mapper;

import com.pethealthcare.demo.dto.request.PaymentCreateRequest;
import com.pethealthcare.demo.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentCreateRequest request);
}
