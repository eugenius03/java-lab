package com.car_rental.util;

public class PaymentUtil {
    
    public static boolean isValidAmount(double amount){
        return !ValidationHelper.isNegative(amount);
    }

    public static boolean isValidDateFormat(String date){
        return ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}");
    }
}
