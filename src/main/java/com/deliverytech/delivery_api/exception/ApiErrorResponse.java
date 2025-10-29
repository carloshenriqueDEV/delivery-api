package com.deliverytech.delivery_api.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public record ApiErrorResponse(
    int status,
    String error, 
    String message, 
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp,
    String path,
    String errorCode,
    Map<String, String> details
    )  {}
