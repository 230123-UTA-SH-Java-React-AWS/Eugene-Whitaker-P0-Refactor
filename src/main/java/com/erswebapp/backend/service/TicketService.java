package com.erswebapp.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.erswebapp.backend.model.Ticket;
import com.erswebapp.backend.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAllByEmployeeId(UUID id) {
        return ticketRepository.findAllByEmployeeId(id);
    }

    public Ticket save(Ticket ticket) throws DataIntegrityViolationException {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findById(UUID id) {
        return ticketRepository.findById(id);
    }

    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }
}
