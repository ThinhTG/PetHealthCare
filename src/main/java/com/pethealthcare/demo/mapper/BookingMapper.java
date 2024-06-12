package com.pethealthcare.demo.mapper;

import com.pethealthcare.demo.dto.request.BookingCreateRequest;
import com.pethealthcare.demo.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "bookingDetails", ignore = true)
    Booking toBooking(BookingCreateRequest bookingCreateRequest);
}
