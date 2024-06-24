package com.pethealthcare.mapper;

import com.pethealthcare.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.model.BookingDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingDetailMapper {
    BookingDetail toBookingDetail(BookingDetailCreateRequest request);
}
