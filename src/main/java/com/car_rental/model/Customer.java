package com.car_rental.model;

import com.car_rental.util.CustomerUtil;

public record Customer (String firstName, String lastName, String driverLicense, String birthDate) {

    public Customer{
        CustomerUtil.validateName(firstName);
        CustomerUtil.validateName(lastName);
        CustomerUtil.validateLicense(driverLicense);
        CustomerUtil.validateDate(birthDate);
    }

}
