package com.car_rental.util;

import java.util.regex.Pattern;

class ValidationHelper {
    
    static boolean isNumberBetween(int number, int min, int max){
        return number >= min && number <= max;
    }

    static boolean isStringMatchPattern(String text, String pattern){
        if (text == null || pattern == null) return false;
        return Pattern.matches(pattern, text);
    }

    static boolean isStringLengthBetween(String text, int min, int max){
        if (text == null) return false;
        int length = text.trim().length();
        return length >= min && length <= max;
    }

    static boolean isNegative (double number){
        return number < 0.0;
    }

    static boolean isNegative (int number){
        return number < 0.0;
    }

}
