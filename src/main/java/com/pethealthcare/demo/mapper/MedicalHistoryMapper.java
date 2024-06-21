package com.pethealthcare.demo.mapper;

import com.pethealthcare.demo.dto.request.MedicalHistoryCreateRequest;
import com.pethealthcare.demo.model.MedicalHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {
    MedicalHistory toMedicalHistory(MedicalHistoryCreateRequest request);
}
