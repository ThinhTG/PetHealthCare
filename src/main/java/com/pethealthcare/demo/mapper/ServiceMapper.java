package com.pethealthcare.demo.mapper;


import com.pethealthcare.demo.dto.request.ServiceCreateRequest;
import com.pethealthcare.demo.model.Services;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "bookingDetails", ignore = true)
    Services toService(ServiceCreateRequest request);
}
