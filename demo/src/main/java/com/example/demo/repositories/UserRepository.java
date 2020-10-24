package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    public Optional<User> findByEmail(String email);
}
