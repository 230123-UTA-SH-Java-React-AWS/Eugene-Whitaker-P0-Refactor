package com.erswebapp.backend.model;

public enum Status {
    PENDING {
        @Override
        public boolean isPending() {
            return true;
        }
    },
    DENIED {
        @Override
        public boolean isDenied() {
            return true;
        }
    },
    APPROVED {
        @Override
        public boolean isApproved() {
            return true;
        }
    };

    public boolean isPending() {
        return false;
    }

    public boolean isDenied() {
        return false;
    }

    public boolean isApproved() {
        return false;
    }

    private Status() {
    }
}
