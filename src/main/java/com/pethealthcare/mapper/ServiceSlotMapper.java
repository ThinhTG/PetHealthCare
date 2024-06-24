package com.pethealthcare.mapper;

import com.pethealthcare.dto.request.ServiceSlotCreateRequest;
import com.pethealthcare.model.ServiceSlot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceSlotMapper {
    ServiceSlot toServiceSlot(ServiceSlotCreateRequest request);
}
