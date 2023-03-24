package com.erswebapp.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erswebapp.backend.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> findAllByEmployeeId(UUID id);
}
