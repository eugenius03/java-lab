package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import com.car_rental.util.CustomerUtil;

public record Customer (
    String firstName,
    String lastName,
    String driverLicense,
    String birthDate
    ) implements Comparable<Customer> {

    public Customer{
        CustomerUtil.validateName(firstName);
        CustomerUtil.validateName(lastName);
        CustomerUtil.validateLicense(driverLicense);
        CustomerUtil.validateDate(birthDate);
    }

    @Override
    public int compareTo(Customer o){
        return this.driverLicense.compareTo(o.driverLicense());
    }

    public static Comparator<Customer> byFirstName(){
        return Comparator.comparing(Customer::firstName);
    }

    public static Comparator<Customer> byLastName(){
        return Comparator.comparing(Customer::lastName);
    }

    public static Comparator<Customer> byBirthDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return Comparator.comparing(c -> LocalDate.parse(c.birthDate(), formatter));
    }

}
