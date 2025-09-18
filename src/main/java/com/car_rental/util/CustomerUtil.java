package com.car_rental.util;

public class CustomerUtil {

    public static void validateName(String name){
        if(!ValidationHelper.isStringLengthBetween(name, 1, 30)){
            throw new IllegalArgumentException("Invalid customer name");
        }
    }

    public static void validateLicense(String driverLicense){
        if(!ValidationHelper.isStringMatchPattern(driverLicense, "([А-Я]{3})([0-9]{6})")){
            throw new IllegalArgumentException("Invalid driver license. Expected format: XXX123456, but got " + driverLicense);
        }
    }

    public static void validateDate(String date){
        if(!ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}")){
            throw new IllegalArgumentException("Invalid date format. Expected format: dd.MM.yyyy, but got " + date);
        }
    }
    
}
