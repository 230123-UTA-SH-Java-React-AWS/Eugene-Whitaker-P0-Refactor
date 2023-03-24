package com.erswebapp.backend.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erswebapp.backend.exception.InvalidAccountException;
import com.erswebapp.backend.exception.InvalidActionException;
import com.erswebapp.backend.model.Manager;
import com.erswebapp.backend.model.Role;
import com.erswebapp.backend.model.Status;
import com.erswebapp.backend.model.Ticket;
import com.erswebapp.backend.service.ManagerService;
import com.erswebapp.backend.service.TicketService;
import com.erswebapp.backend.utilities.BaseInfo;
import com.erswebapp.backend.utilities.ErrorResponseInfo;
import com.erswebapp.backend.utilities.ResponseInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

// TODO: add change roles feature
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/ers")
public class ManagerController {
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private final ManagerService managerService;
    private final TicketService ticketService;

    @Autowired
    public ManagerController(ManagerService managerService, TicketService ticketService) {
        this.managerService = managerService;
        this.ticketService = ticketService;
    }

    @PostMapping(path = "/manager/login")
    public ResponseEntity<? extends BaseInfo> processLogin(@RequestBody @Valid Manager manager,
            HttpServletRequest request) {
        Date date = new Date();
        if (manager.getEmail() != null || manager.getUsername() != null) {
            Optional<Manager> foundManager = managerService.authorizeLogin(manager);
            if (foundManager.isPresent()) {
                ResponseInfo<Manager> response = new ResponseInfo<Manager>(
                        DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK,
                        request.getRequestURI(), foundManager.get());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } else {
            throw new InvalidAccountException("This account contains neither an email address nor a password");
        }
        ErrorResponseInfo<Manager> error = new ErrorResponseInfo<Manager>(
                DATE_FORMAT.format(new Timestamp(date.getTime())),
                HttpStatus.BAD_REQUEST, request.getRequestURI(), "Invalid login credentials", manager);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @PostMapping(path = "/manager/register")
    public ResponseEntity<? extends BaseInfo> processRegistration(@RequestBody @Valid Manager manager,
            HttpServletRequest request) {
        Date date = new Date();
        try {
            manager.setRole(Role.MANAGER);
            Manager newManager = managerService.save(manager);
            ResponseInfo<Manager> response = new ResponseInfo<Manager>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK,
                    request.getRequestURI(), newManager);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataIntegrityViolationException e) {
            ErrorResponseInfo<Manager> error = new ErrorResponseInfo<Manager>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())),
                    HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    "Invalid registration request, either the email or the username are incorrect, or they already exists",
                    manager);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping(path = "/manager/{id}/profile")
    public ResponseEntity<? extends BaseInfo> processUpdateProfile(@PathVariable(value = "id") final UUID id,
            @RequestBody @Valid Manager updatedManager, HttpServletRequest request) {
        Date date = new Date();
        try {
            updatedManager.setId(id);
            updatedManager.setRole(Role.MANAGER);
            Manager newManager = managerService.save(updatedManager);
            ResponseInfo<Manager> response = new ResponseInfo<Manager>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK,
                    request.getRequestURI(), newManager);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataIntegrityViolationException e) {
            ErrorResponseInfo<Manager> error = new ErrorResponseInfo<Manager>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())),
                    HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    "Invalid registration request, either the email or the username are incorrect, or they already exists",
                    updatedManager);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping(path = "/manager/{id}/tickets")
    public ResponseEntity<? extends BaseInfo> getAllTickets(@PathVariable(value = "id") final UUID id,
            HttpServletRequest request) {
        List<Ticket> tickets = ticketService.findAll();

        Date date = new Date();
        ResponseInfo<List<Ticket>> response = new ResponseInfo<List<Ticket>>(
                DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK, request.getRequestURI(), tickets);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/manager/{managerId}/tickets/{ticketId}")
    public ResponseEntity<? extends BaseInfo> processTicketByAction(
            @PathVariable(value = "managerId") final UUID managerId,
            @PathVariable(value = "ticketId") final UUID ticketId, @RequestParam String action,
            HttpServletRequest request) {
        Ticket ticket = ticketService.findById(ticketId).get();

        Date date = new Date();
        switch (action) {
            case "APPROVED":
                ticket.setStatus(Status.APPROVED);
                ticket.setManagerId(managerId);
                ticket.setProcessedTimestamp(DATE_FORMAT.format(new Timestamp(date.getTime())));
                break;
            case "DENIED":
                ticket.setStatus(Status.DENIED);
                ticket.setManagerId(managerId);
                ticket.setProcessedTimestamp(DATE_FORMAT.format(new Timestamp(date.getTime())));
                break;
            default:
                throw new InvalidActionException("The actions: " + action + " is incorrect");
        }

        try {
            Ticket newTicket = ticketService.save(ticket);
            ResponseInfo<Ticket> response = new ResponseInfo<Ticket>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK,
                    request.getRequestURI(), newTicket);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataIntegrityViolationException e) {
            ErrorResponseInfo<Ticket> error = new ErrorResponseInfo<Ticket>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())),
                    HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    "Invalid new ticket request, either the amount or the discription is missing", ticket);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
