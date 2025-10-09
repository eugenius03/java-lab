package com.car_rental.util;

import com.car_rental.exception.InvalidDataException;
import com.car_rental.model.Car;
import com.car_rental.model.CarStatus;

public class RentalUtil {

    public static boolean isValidId(int id){
        return ValidationHelper.isNegative(id);
    }

    public static void ValidateId(int id){
        if(!isValidId(id)){
            throw new InvalidDataException("Invaid id, must be non-negative");
        }
    }
    
    public static boolean isValidDateFormat(String date){
        return ValidationHelper.isStringMatchPattern(date, "\\d{2}\\.\\d{2}\\.\\d{4}");
    }

    public static void ValidateDateFormat(String date){
        if(!isValidDateFormat(date)){
            throw new InvalidDataException("Invalid date format. Expected format: dd.MM.yyyy, but got " + date);
        }
    }

    public static void validateCar(Car car){
        if(car.getStatus() != CarStatus.AVAILABLE){
            throw new InvalidDataException("Car is not available for rental");
        }
    }
}
