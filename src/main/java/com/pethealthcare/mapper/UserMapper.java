package com.pethealthcare.mapper;

import com.pethealthcare.dto.request.UserCreateRequest;
import com.pethealthcare.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
