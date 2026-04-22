package com.example.purchasereportingapi.repository;

import com.example.purchasereportingapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
