package com.example.mininotes.repository;

import com.example.mininotes.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserRep extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}

