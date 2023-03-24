package com.erswebapp.backend.model;

public enum ReimbursmentType {
    TRAVEL {
        @Override
        public boolean isTravel() {
            return true;
        }
    },
    LODGING {
        @Override
        public boolean isLodging() {
            return true;
        }
    },
    FOOD {
        @Override
        public boolean isFood() {
            return true;
        }
    },
    OTHER {
        @Override
        public boolean isOther() {
            return true;
        }
    };

    public boolean isTravel() {
        return false;
    }

    public boolean isLodging() {
        return false;
    }

    public boolean isFood() {
        return false;
    }

    public boolean isOther() {
        return false;
    }

    private ReimbursmentType() {
    }
}
