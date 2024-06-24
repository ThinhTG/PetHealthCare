package com.pethealthcare.mapper;

import com.pethealthcare.dto.request.PaymentCreateRequest;
import com.pethealthcare.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toPayment(PaymentCreateRequest request);
}
