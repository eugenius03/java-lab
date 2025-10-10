package com.car_rental.util;

import com.car_rental.exception.InvalidDataException;

public class PaymentUtil {

    public static boolean isValidId(int id){
        return !ValidationHelper.isNegative(id);
    }

    public void validateId(int id){
        if(!isValidId(id)){
            throw new InvalidDataException("Invalid id, must be non-negative");
        }
    }
    
    public static boolean isValidAmount(double amount){
        return !ValidationHelper.isNegative(amount);
    }

    public static void validateAmount(double amount){
        if(!isValidAmount(amount)){
            throw new InvalidDataException("Invalid amount, must be non-negative");
        }
    }

    public static boolean isValidDateFormat(String date){
        return ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}");
    }

    public static void validateDateFormat(String date){
        if(!isValidDateFormat(date)){
            throw new InvalidDataException("Invalid date format. Expected format: dd.MM.yyyy, but got " + date);
        }
    }
}
