package com.car_rental.util;

public class RentalUtil {
    
    public static boolean isValidDateFormat(String date){
        return ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}");
    }
}
