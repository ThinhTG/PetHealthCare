package com.pethealthcare.demo.mapper;


import com.pethealthcare.demo.dto.request.ServiceCreateRequest;
import com.pethealthcare.demo.model.Services;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Services toService(ServiceCreateRequest request);
}
