package com.erswebapp.backend.model;

import com.erswebapp.backend.utilities.RoleConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Account {
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^[^@]*$")
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = false)
    private String password;

    @Column(name = "phone_number", nullable = true, unique = false)
    private String phoneNumber;

    @Column(nullable = true, unique = false)
    private String address;

    @Column(name = "first_name", nullable = true, unique = false)
    private String firstName;

    @Column(name = "last_name", nullable = true, unique = false)
    private String lastName;

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false, unique = false)
    private Role role;

    @JsonIgnore
    public boolean isEmployee() {
        return this.role.isEmployee();
    }

    @JsonIgnore
    public boolean isManager() {
        return this.role.isManager();
    }
}
