package com.car_rental.model;

public enum CarStatus {
    AVAILABLE(true),
    RENTED(false),
    MAINTENANCE(false),
    RESERVED(false);

    private boolean isAvailable;
    
    CarStatus(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
    }
}
