package com.workintech.spring17challenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiErrorResponse {
    private Integer status;
    private String message;
    private Long timestamp;
}
