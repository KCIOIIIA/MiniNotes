package com.example.mininotes.repository;

import com.example.mininotes.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UserRep extends JpaRepository<User, Long> {
    public Optional<User> getUserByName(String name);
}

