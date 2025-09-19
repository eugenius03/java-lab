package com.car_rental.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.car_rental.exception.InvalidDataException;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    CASH,
    ONLINE;

    private static final Logger logger = Logger.getLogger(PaymentMethod.class.getName());

    public static PaymentMethod parsePaymentMethod(String paymentMethod){
        logger.log(Level.INFO, "Trying to parse from {0}", paymentMethod);
        
        if (paymentMethod == null){
            logger.log(Level.WARNING, "Attempted to parse from null String");
            throw new InvalidDataException("PaymentMethod can't be null");
        }
        try {
            PaymentMethod result = PaymentMethod.valueOf(paymentMethod.trim().toUpperCase());
            logger.log(Level.INFO, "Successfully parsed PaymentMethod {0}", result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Failed to parse PaymentMethod, {0} is invalid", paymentMethod);
            throw new InvalidDataException("Invalid PaymentMethod: '" + paymentMethod + "'. Should be one of: " +
                java.util.Arrays.toString(PaymentMethod.values()) + ", but got: " + paymentMethod);
        }
        
    }
}
