package com.example.purchasereportingapi.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(Long id, String name, String email) {
}
