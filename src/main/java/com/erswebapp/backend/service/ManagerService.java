package com.erswebapp.backend.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.erswebapp.backend.model.Manager;
import com.erswebapp.backend.repository.ManagerRepository;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public Optional<Manager> authorizeLogin(Manager manager) {
        Optional<Manager> optionalManager = managerRepository.findByEmailOrUsername(manager.getEmail(),
                manager.getUsername());
        if (optionalManager.isPresent()) {
            if (manager.getPassword().equals(optionalManager.get().getPassword())) {
                return optionalManager;
            }
        }

        return Optional.empty();
    }

    public Manager save(Manager manager) throws DataIntegrityViolationException {
        return managerRepository.save(manager);
    }

    public Optional<Manager> findById(UUID id) {
        return managerRepository.findById(id);
    }
}
