package com.car_rental.util;

public class PaymentUtil {
    
    public static boolean isValidAmount(double amount){
        return !ValidationHelper.isNegative(amount);
    }

    public static void validateAmount(double amount){
        if(!isValidAmount(amount)){
            throw new IllegalArgumentException("Invalid amount, must be non-negative");
        }
    }

    public static boolean isValidDateFormat(String date){
        return ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}");
    }

    public static void validateDateFormat(String date){
        if(!isValidDateFormat(date)){
            throw new IllegalArgumentException("Invalid date format. Expected format: dd.MM.yyyy, but got " + date);
        }
    }
}
