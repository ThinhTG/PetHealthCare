package com.pethealthcare.demo.mapper;


import com.pethealthcare.demo.dto.request.PetCreateRequest;
import com.pethealthcare.demo.model.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetMapper {
    Pet toPet(PetCreateRequest request);
}
