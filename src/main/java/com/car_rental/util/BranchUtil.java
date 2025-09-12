package com.car_rental.util;

public class BranchUtil {
    
    public static boolean isValidName(String name){
        return ValidationHelper.isStringLengthBetween(name, 1, 50);
    }

    public static boolean isValidLocation(String location){
        return ValidationHelper.isStringLengthBetween(location, 1, 50);
    }
}
