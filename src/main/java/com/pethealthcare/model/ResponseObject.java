package com.pethealthcare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
    private String status;
    private String message;
    private Object data;

    public ResponseObject(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
