package com.example.purchasereportingapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.dto.request.CreateUserRequest;
import com.example.purchasereportingapi.dto.response.UserResponse;
import com.example.purchasereportingapi.entity.User;
import com.example.purchasereportingapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Ana");
        request.setEmail("ana@email.com");

        when(userRepository.save(any(User.class))).thenReturn(User.builder()
                .id(1L)
                .name("Ana")
                .email("ana@email.com")
                .build());

        UserResponse response = userService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Ana");
    }
}
