package com.pethealthcare.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CageUpdateRequest {
    private int cageId;
    private boolean status;
}
