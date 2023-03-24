package com.erswebapp.backend.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.erswebapp.backend.model.Employee;
import com.erswebapp.backend.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Optional<Employee> authorizeLogin(Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmailOrUsername(employee.getEmail(),
                employee.getUsername());
        if (optionalEmployee.isPresent()) {
            if (employee.getPassword().equals(optionalEmployee.get().getPassword())) {
                return optionalEmployee;
            }
        }

        return Optional.empty();
    }

    public Employee save(Employee employee) throws DataIntegrityViolationException {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findById(UUID id) {
        return employeeRepository.findById(id);
    }
}
