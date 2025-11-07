package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import com.car_rental.util.ValidationUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Customer (

    @NotBlank(message = "First name cannot be null or blank")
    @Pattern(
        regexp = "^[\\p{L}\\s\\-']{2,50}$",
        message = "First name must be 2-50 characters long and contain only letters, spaces, hyphens, or apostrophes"
    )
    String firstName,

    @NotBlank(message = "Last name cannot be null or blank")
    @Pattern(
        regexp = "^[\\p{L}\\s\\-']{2,50}$",
        message = "Last name must be 2-50 characters long and contain only letters, spaces, hyphens, or apostrophes"
    )
    String lastName,

    @NotBlank(message = "Driver license cannot be null or blank")
    @Pattern(
        regexp = "^[А-Я]{3}([0-9]{6})$",
        message = "Driver license must match format XXX123456"
    )
    String driverLicense,

    @NotBlank(message = "Birth date cannot be null or blank")
    @Pattern(
        regexp = "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(19|20)\\d\\d$",
        message = "Birth date must match format DD.MM.YYYY"
    )
    String birthDate
    ) implements Comparable<Customer> {

    public Customer(String firstName, String lastName, String driverLicense, String birthDate){
        this.firstName = firstName;
        this.lastName = lastName;
        this.driverLicense = driverLicense;
        this.birthDate = birthDate;

        ValidationUtil.validate(this);
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
