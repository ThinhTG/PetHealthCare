package com.pethealthcare.demo.mapper;


import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {
    @Mapping(target = "bookingDetails", ignore = true)
    Pet toPet(PetCreateRequest request);
}
