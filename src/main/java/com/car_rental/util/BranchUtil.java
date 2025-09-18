package com.car_rental.util;

import com.car_rental.exception.InvalidDataException;

public class BranchUtil {
    
    public static void validateName(String name){
        if(!ValidationHelper.isStringLengthBetween(name, 1, 50)){
            throw new InvalidDataException("Invalid branch name, must be between 1 and 50 characters" );
        }
    }

    public static void validateLocation(String location){
        if(!ValidationHelper.isStringLengthBetween(location, 1, 50)){
            throw new InvalidDataException("Invalid branch location, must be between 1 and 50 characters" );
        }
    }
}
