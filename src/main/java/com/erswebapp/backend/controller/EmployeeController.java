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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erswebapp.backend.exception.InvalidAccountException;
import com.erswebapp.backend.model.Employee;
import com.erswebapp.backend.model.Role;
import com.erswebapp.backend.model.Status;
import com.erswebapp.backend.model.Ticket;
import com.erswebapp.backend.service.EmployeeService;
import com.erswebapp.backend.service.TicketService;
import com.erswebapp.backend.utilities.BaseInfo;
import com.erswebapp.backend.utilities.ErrorResponseInfo;
import com.erswebapp.backend.utilities.ResponseInfo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/ers")
public class EmployeeController {

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private final EmployeeService employeeService;
    private final TicketService ticketService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, TicketService ticketService) {
        this.employeeService = employeeService;
        this.ticketService = ticketService;
    }

    @PostMapping(path = "/employee/login")
    public ResponseEntity<? extends BaseInfo> processLogin(@RequestBody @Valid Employee employee,
            HttpServletRequest request) {
        Date date = new Date();
        if (employee.getEmail() != null || employee.getUsername() != null) {
            Optional<Employee> foundEmployee = employeeService.authorizeLogin(employee);
            if (foundEmployee.isPresent()) {
                ResponseInfo<Employee> response = new ResponseInfo<Employee>(
                        DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK,
                        request.getRequestURI(), foundEmployee.get());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } else {
            throw new InvalidAccountException("This account contains neither an email address nor a password");
        }
        ErrorResponseInfo<Employee> error = new ErrorResponseInfo<Employee>(
                DATE_FORMAT.format(new Timestamp(date.getTime())),
                HttpStatus.BAD_REQUEST, request.getRequestURI(), "Invalid login credentials", employee);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @PostMapping(path = "/employee/register")
    public ResponseEntity<? extends BaseInfo> processRegistration(@RequestBody @Valid Employee employee,
            HttpServletRequest request) {
        Date date = new Date();
        try {
            employee.setRole(Role.EMPLOYEE);
            Employee newEmployee = employeeService.save(employee);
            ResponseInfo<Employee> response = new ResponseInfo<Employee>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK,
                    request.getRequestURI(), newEmployee);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataIntegrityViolationException e) {
            ErrorResponseInfo<Employee> error = new ErrorResponseInfo<Employee>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())),
                    HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    "Invalid registration request, either the email or the username are incorrect, or they already exists",
                    employee);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping(path = "/employee/{id}/profile")
    public ResponseEntity<? extends BaseInfo> processUpdateProfile(@PathVariable(value = "id") final UUID id,
            @RequestBody @Valid Employee updatedEmployee, HttpServletRequest request) {
        Date date = new Date();
        try {
            updatedEmployee.setId(id);
            updatedEmployee.setRole(Role.EMPLOYEE);
            Employee newEmployee = employeeService.save(updatedEmployee);
            ResponseInfo<Employee> response = new ResponseInfo<Employee>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK,
                    request.getRequestURI(), newEmployee);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DataIntegrityViolationException e) {
            ErrorResponseInfo<Employee> error = new ErrorResponseInfo<Employee>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())),
                    HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    "Invalid registration request, either the email or the username are incorrect, or they already exists",
                    updatedEmployee);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping(path = "/employee/{id}/tickets")
    public ResponseEntity<? extends BaseInfo> getAllTicketsByEmployeeId(@PathVariable(value = "id") final UUID id,
            HttpServletRequest request) {
        List<Ticket> tickets = ticketService.findAllByEmployeeId(id);

        Date date = new Date();
        ResponseInfo<List<Ticket>> response = new ResponseInfo<List<Ticket>>(
                DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.OK, request.getRequestURI(), tickets);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path = "/employee/{id}/tickets")
    public ResponseEntity<? extends BaseInfo> processNewTicket(@PathVariable(value = "id") final UUID id,
            @RequestBody @Valid Ticket ticket, HttpServletRequest request) {
        Date date = new Date();
        Employee employee = employeeService.findById(id).get();
        ticket.setEmployee(employee);
        ticket.setStatus(Status.PENDING);
        ticket.setDateAdded(DATE_FORMAT.format(new Timestamp(date.getTime())));
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

    @PatchMapping(path = "/employee/{employeeId}/tickets/{ticketId}")
    public ResponseEntity<? extends BaseInfo> processReSubmitTicket(
            @PathVariable(value = "ticketId") final UUID ticketId,
            @RequestBody @Valid Ticket ticket, HttpServletRequest request) {
        Date date = new Date();
        if (!ticket.isPending()) {
            ticketService.delete(ticket);
            try {
                ticket.setId(ticketId);
                ticket.setStatus(Status.PENDING);
                ticket.setManagerId(null);
                ticket.setDateProcessed(null);
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
        } else {
            ErrorResponseInfo<Ticket> error = new ErrorResponseInfo<Ticket>(
                    DATE_FORMAT.format(new Timestamp(date.getTime())), HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    "Invalid resubmit ticket request, this ticket is still pending", ticket);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
