package com.example.purchasereportingapi.exception;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Builder;

@Builder
public record ApiErrorResponse(LocalDateTime timestamp, int status, String error, String message,
                               Map<String, String> validations) {
}
