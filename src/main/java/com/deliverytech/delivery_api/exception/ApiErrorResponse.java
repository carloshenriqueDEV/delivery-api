package com.deliverytech.delivery_api.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(int status, String error, String message, LocalDateTime timestamp)  {}
