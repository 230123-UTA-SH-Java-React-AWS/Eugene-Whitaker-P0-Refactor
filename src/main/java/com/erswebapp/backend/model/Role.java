package com.erswebapp.backend.model;

public enum Role {
    EMPLOYEE {
        @Override
        public boolean isEmployee() {
            return true;
        }
    },
    MANAGER {
        @Override
        public boolean isManager() {
            return true;
        }
    };

    public boolean isEmployee() {
        return false;
    }

    public boolean isManager() {
        return false;
    }

    private Role() {
    }
}
