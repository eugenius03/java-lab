package com.car_rental.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.car_rental.exception.InvalidDataException;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    CASH,
    ONLINE;

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethod.class);

    public static PaymentMethod parsePaymentMethod(String paymentMethod){
        logger.info("Trying to parse from {}", paymentMethod);
        
        if (paymentMethod == null){
            logger.warn("Attempted to parse from null String");
            throw new InvalidDataException("PaymentMethod can't be null");
        }
        try {
            PaymentMethod result = PaymentMethod.valueOf(paymentMethod.trim().toUpperCase());
            logger.info("Successfully parsed PaymentMethod {}", result);
            return result;
        } catch (IllegalArgumentException e) {
            logger.error( "Failed to parse PaymentMethod, {0} is invalid", paymentMethod);
            throw new InvalidDataException("Invalid PaymentMethod: '" + paymentMethod + "'. Should be one of: " +
                java.util.Arrays.toString(PaymentMethod.values()) + ", but got: " + paymentMethod);
        }
        
    }
}
