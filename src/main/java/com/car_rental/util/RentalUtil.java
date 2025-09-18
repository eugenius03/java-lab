package com.car_rental.util;

import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

public class RentalUtil {
    
    public static boolean isValidDateFormat(String date){
        return ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}");
    }

    public static void ValidateDateFormat(String date){
        if(!isValidDateFormat(date)){
            throw new IllegalArgumentException("Invalid date format. Expected format: dd.MM.yyyy, but got " + date);
        }
    }

    public static void validateCar(Car car){
        if(car.getStatus() != CarStatus.AVAILABLE){
            throw new IllegalArgumentException("Car is not available for rental");
        }
    }
}
