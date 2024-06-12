package com.pethealthcare.demo.mapper;

import com.pethealthcare.demo.dto.request.BookingDetailCreateRequest;
import com.pethealthcare.demo.model.BookingDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingDetailMapper {
    BookingDetail toBookingDetail(BookingDetailCreateRequest request);
}
