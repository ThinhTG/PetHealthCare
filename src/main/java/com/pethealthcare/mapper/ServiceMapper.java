package com.pethealthcare.mapper;


import com.pethealthcare.dto.request.ServiceCreateRequest;
import com.pethealthcare.model.Services;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "bookingDetails", ignore = true)
    Services toService(ServiceCreateRequest request);
}
