package com.example.purchasereportingapi.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.purchasereportingapi.dto.request.CreateUserRequest;
import com.example.purchasereportingapi.dto.response.UserResponse;
import com.example.purchasereportingapi.entity.User;
import com.example.purchasereportingapi.exception.BusinessException;
import com.example.purchasereportingapi.exception.ResourceNotFoundException;
import com.example.purchasereportingapi.repository.UserRepository;
import java.util.Optional;
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
    void shouldCreateUserWhenEmailIsAvailable() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Ana");
        request.setEmail("ana@email.com");

        when(userRepository.findByEmail("ana@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(User.builder()
                .id(1L)
                .name("Ana")
                .email("ana@email.com")
                .build());

        UserResponse response = userService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Ana");
        assertThat(response.email()).isEqualTo("ana@email.com");
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Ana");
        request.setEmail("ana@email.com");

        when(userRepository.findByEmail("ana@email.com"))
                .thenReturn(Optional.of(User.builder().id(99L).name("Otra Ana").email("ana@email.com").build()));

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Ya existe un usuario con email: ana@email.com");
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(User.builder().id(1L).name("Ana").email("ana@email.com").build()));

        UserResponse response = userService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Ana");
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuario no encontrado con id: 99");

        verify(userRepository).findById(99L);
    }
}
