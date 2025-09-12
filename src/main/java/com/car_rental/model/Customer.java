package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.car_rental.util.CustomerUtil;

public class Customer {
    private String firstName;
    private String lastName;
    private String driverLicense;
    private LocalDate birthDate;

    public Customer(){

    }

    public Customer(String firstName, String lastName, String driverLicense, String birthDate){
        setFirstName(firstName);
        setLastName(lastName);
        setDriverLicense(driverLicense);
        setBirthDate(birthDate);
    }

    public void setFirstName(String firstName){
        if (CustomerUtil.isValidFirstName(firstName)){
            this.firstName = firstName;
        }
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){
        if (CustomerUtil.isValidLastName(lastName)){
            this.lastName = lastName;
        }
    }

    public String getLastName(){
        return lastName;
    }

    public void setDriverLicense(String driverLicense){
        driverLicense = driverLicense.trim().toUpperCase();
        if (CustomerUtil.isValidDriverLicense(driverLicense)){
            this.driverLicense = driverLicense;
        }
    }

    public String getDriverLicense(){
        return driverLicense;
    }

    public void setBirthDate(String birthDate){
        if (CustomerUtil.isValidDateFormat(birthDate)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            this.birthDate = LocalDate.parse(birthDate, formatter);
        }
    }

    public LocalDate getBirthDate(){
        return birthDate;
    }

    public static Customer createCustomer(String firstName, String lastName, String driverLicense, String birthDate){
        if (CustomerUtil.isValidFirstName(firstName) && CustomerUtil.isValidLastName(lastName)
            && CustomerUtil.isValidDriverLicense(driverLicense) && CustomerUtil.isValidDateFormat(birthDate))
            return new Customer(firstName, lastName, driverLicense, birthDate);
        
        return null;
    }

    @Override
    public String toString(){
        return String.format("Customer{firstName=%s, lastName=%s, driverLicense=%s, birthDate=%s}",
             firstName, lastName, driverLicense, birthDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return  Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(driverLicense, customer.driverLicense)&&
                Objects.equals(birthDate, customer.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, driverLicense, birthDate);
    }


}
