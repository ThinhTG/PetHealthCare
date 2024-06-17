package com.pethealthcare.demo.mapper;

import com.pethealthcare.demo.dto.request.UserCreateRequest;
import com.pethealthcare.demo.dto.request.UserUpdateRequest;
import com.pethealthcare.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(target = "bookings", ignore = true),
            @Mapping(target = "pets", ignore = true),
            @Mapping(target = "bookingDetails", ignore = true),
            @Mapping(target = "serviceSlots", ignore = true)
    })
    User toUser(UserCreateRequest request);
}
