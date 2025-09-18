package com.car_rental.util;

import java.time.LocalDate;

public class CarUtil {

    public static boolean isValidLicensePlate(String licensePlate){
        if (licensePlate == null) {
            return false;
        }
        licensePlate = licensePlate.trim().toUpperCase();
        String standartRegex = "^[АВЕКМНОРСТІХDIPYCE]{2}\\d{4}[АВЕКМНОРСТІХDIPYCE]{2}$";
        String individualRegex = "^(?!.*[ZV])[а-яА-ЯЇЄІҐїєіїa-zA-Z0-9]{3,8}$";
        return ValidationHelper.isStringMatchPattern(licensePlate, standartRegex) || ValidationHelper.isStringMatchPattern(licensePlate, individualRegex);
    }

    public static void validateLicensePlate(String licensePlate){
        if(!isValidLicensePlate(licensePlate)){
            throw new IllegalArgumentException("Invalid license plate. Expected format: XX1234XX or custom 3-8 chars, but got " + licensePlate);
        }
    }

    public static boolean isValidModel(String model){
        return ValidationHelper.isStringLengthBetween(model, 5, 100);
    }

    public static void validateModel(String model){
        if(!isValidModel(model)){
            throw new IllegalArgumentException("Invalid model name, expected length between 5 and 100 characters");
        }
    }
    
    public static boolean isValidYear(int year){
        return ValidationHelper.isNumberBetween(year, LocalDate.now().getYear()-40, LocalDate.now().getYear());
    }

    public static void validateYear(int year){
        if(!isValidYear(year)){
            throw new IllegalArgumentException("Invalid year, expected between " + (LocalDate.now().getYear()-40) + " and " + LocalDate.now().getYear());
        }
    }

    public static boolean isValidMileage(double mileage){
        return !ValidationHelper.isNegative(mileage);
    }

    public static void validateMileage(double mileage){
        if(!isValidMileage(mileage)){
            throw new IllegalArgumentException("Invalid mileage, must be non-negative");
        }
    }
}
