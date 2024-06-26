package com.pethealthcare.demo.dto.request;

import com.pethealthcare.demo.model.CageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CageCreateRequest {
    private CageType type;

}
