package com.car_rental.util;

import java.time.LocalDate;

public class CarUtil {

    public static boolean isValidLicensePlate(String licensePlate){
        String standartRegex = "^[АВЕКМНОРСТІХDIPYCE]{2}\\d{4}[АВЕКМНОРСТІХDIPYCE]{2}$";
        String individualRegex = "^(?!.*[ZV])[а-яА-Яa-zA-Z0-9]{3,8}$";
        return ValidationHelper.isStringMatchPattern(licensePlate, standartRegex) || ValidationHelper.isStringMatchPattern(licensePlate, individualRegex);
    }

    public static boolean isValidModel(String model){
        return ValidationHelper.isStringLengthBetween(model, 5, 100);
    }
    
    public static boolean isValidYear(int year){
        return ValidationHelper.isNumberBetween(year, LocalDate.now().getYear()-40, LocalDate.now().getYear());
    }

    public static boolean isValidMileage(double mileage){
        return !ValidationHelper.isNegative(mileage);
    }
}
