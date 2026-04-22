package com.example.purchasereportingapi.service;

import com.example.purchasereportingapi.dto.request.CreateUserRequest;
import com.example.purchasereportingapi.dto.response.UserResponse;
import java.util.List;

public interface UserService {

    UserResponse create(CreateUserRequest request);

    List<UserResponse> findAll();

    UserResponse findById(Long id);
}
