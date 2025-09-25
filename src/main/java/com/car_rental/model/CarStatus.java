package com.car_rental.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.exception.InvalidDataException;

public enum CarStatus {
    AVAILABLE(true),
    RENTED(false),
    MAINTENANCE(false),
    RESERVED(false);

    private boolean isAvailable;
    private static final Logger logger = LoggerFactory.getLogger(CarStatus.class);
    
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
        logger.info( "Trying to parse from {}", status);
        
        if (status == null){
            logger.warn("Attempted to parse from null String");
            throw new InvalidDataException("CarStatus can't be null");
        }
        try {
            CarStatus result = CarStatus.valueOf(status.trim().toUpperCase());
            logger.info("Successfully parsed CarStatus {}", result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error( "Failed to parse CarStatus, {} is invalid", status);
            throw new InvalidDataException("Invalid CarStatus: '" + status + "'. Should be one of: " +
                java.util.Arrays.toString(CarStatus.values()) + ", but got: " + status);
        }
        
    }
}
