package com.pethealthcare.demo.mapper;

import com.pethealthcare.demo.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.demo.model.ServiceSlot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceSlotMapper {
    ServiceSlot toServiceSlot(ServiceSlotCreateRequest request);
}
