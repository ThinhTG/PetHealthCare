package com.pethealthcare.mapper;

import com.pethealthcare.dto.request.BookingCreateRequest;
import com.pethealthcare.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "bookingDetails", ignore = true)
    Booking toBooking(BookingCreateRequest bookingCreateRequest);
}
