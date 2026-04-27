package com.example.purchasereportingapi.service.impl;

import com.example.purchasereportingapi.dto.request.CreateUserRequest;
import com.example.purchasereportingapi.dto.response.UserResponse;
import com.example.purchasereportingapi.entity.User;
import com.example.purchasereportingapi.exception.BusinessException;
import com.example.purchasereportingapi.exception.ResourceNotFoundException;
import com.example.purchasereportingapi.mapper.EntityMapper;
import com.example.purchasereportingapi.repository.UserRepository;
import com.example.purchasereportingapi.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse create(CreateUserRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(existingUser -> {
                    throw new BusinessException("Ya existe un usuario con email: " + request.getEmail());
                });

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        return EntityMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(EntityMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return EntityMapper.toUserResponse(user);
    }
}
