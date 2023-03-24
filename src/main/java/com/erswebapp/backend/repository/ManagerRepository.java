package com.erswebapp.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erswebapp.backend.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    @Query("SELECT m FROM Manager m WHERE m.email = :email OR m.username = :username")
    public abstract Optional<Manager> findByEmailOrUsername(String email, String username);
}
