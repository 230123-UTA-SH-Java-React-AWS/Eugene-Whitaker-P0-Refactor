package com.erswebapp.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erswebapp.backend.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("SELECT e FROM Employee e WHERE e.email = :email OR e.username = :username")
    public abstract Optional<Employee> findByEmailOrUsername(String email, String username);
}
