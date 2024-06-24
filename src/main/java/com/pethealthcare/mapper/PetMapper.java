package com.pethealthcare.mapper;


import com.pethealthcare.dto.request.PetCreateRequest;
import com.pethealthcare.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {
    @Mapping(target = "bookingDetails", ignore = true)
    Pet toPet(PetCreateRequest request);
}
