package com.car_rental.util;

public class CustomerUtil {

    public static boolean isValidFirstName(String firstName){
        return ValidationHelper.isStringLengthBetween(firstName, 1, 15);
    }

    public static boolean isValidLastName(String lastName){
        return ValidationHelper.isStringLengthBetween(lastName, 1, 20);
    }

    public static boolean isValidDriverLicense(String driverLicense){
        return ValidationHelper.isStringMatchPattern(driverLicense, "([А-Я]{3})([0-9]{6})");
    }

    public static boolean isValidDateFormat(String date){
        return ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}");
    }
    
}
