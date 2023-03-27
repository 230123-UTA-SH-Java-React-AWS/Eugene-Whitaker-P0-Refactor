package com.erswebapp.backend.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.erswebapp.backend.utilities.ReimbursmentTypeConverter;
import com.erswebapp.backend.utilities.StatusConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO: add images blob
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // for frontend type check
    @Transient
    private final String typeGaurd = "ticket";

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false, unique = false)
    private Employee employee;

    @Column(name = "date_added", nullable = false, unique = false)
    private String dateAdded;

    @Column(name = "manager_id", nullable = true, unique = false)
    private UUID managerId;

    @Column(name = "date_processed", nullable = true, unique = false)
    private String dateProcessed;

    @DecimalMin(value = "0.0")
    @Digits(integer = 4, fraction = 3)
    @Column(nullable = false, unique = false)
    private BigDecimal amount;

    @Column(nullable = false, unique = false)
    private String description;

    @Convert(converter = StatusConverter.class)
    @Column(nullable = false, unique = false)
    private Status status;

    @Convert(converter = ReimbursmentTypeConverter.class)
    @Column(nullable = false, unique = false)
    private ReimbursmentType type;

    @JsonIgnore
    public boolean isApproved() {
        return this.status.isApproved();
    }

    @JsonIgnore
    public boolean isDenied() {
        return this.status.isDenied();
    }

    @JsonIgnore
    public boolean isPending() {
        return this.status.isPending();
    }

    @JsonIgnore
    public boolean isTravel() {
        return this.type.isTravel();
    }

    @JsonIgnore
    public boolean isLodging() {
        return this.type.isLodging();
    }

    @JsonIgnore
    public boolean isFood() {
        return this.type.isFood();
    }

    @JsonIgnore
    public boolean isOther() {
        return this.type.isOther();
    }
}
