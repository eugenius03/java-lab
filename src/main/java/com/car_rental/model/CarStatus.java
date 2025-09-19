package com.car_rental.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.car_rental.exception.InvalidDataException;

public enum CarStatus {
    AVAILABLE(true),
    RENTED(false),
    MAINTENANCE(false),
    RESERVED(false);

    private boolean isAvailable;
    private static final Logger logger = Logger.getLogger(CarStatus.class.getName());
    
    CarStatus(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean available) {
        isAvailable = available;
    }

    public static CarStatus parseCarStatus(String status){
        logger.log(Level.INFO, "Trying to parse from {0}", status);
        
        if (status == null){
            logger.log(Level.WARNING, "Attempted to parse from null String");
            throw new InvalidDataException("CarStatus can't be null");
        }
        try {
            CarStatus result = CarStatus.valueOf(status.trim().toUpperCase());
            logger.log(Level.INFO, "Successfully parsed CarStatus {0}", result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Failed to parse CarStatus, {0} is invalid", status);
            throw new InvalidDataException("Invalid CarStatus: '" + status + "'. Should be one of: " +
                java.util.Arrays.toString(CarStatus.values()) + ", but got: " + status);
        }
        
    }
}
