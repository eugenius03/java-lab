package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.car_rental.exception.InvalidDataException;
import com.car_rental.util.RentalUtil;

public class Rental {
    private Car car;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;

    public Rental(){

    }

    public Rental(Car car, Customer customer, String startDate, String endDate){
        setCar(car);
        this.customer = customer;
        setStartDate(startDate);
        setEndDate(endDate);
        if(this.endDate.isBefore(this.startDate)){
            throw new InvalidDataException("End date cannot be before start date");
        }
    }

    public Rental(Car car, Customer customer){
        setCar(car);
        this.customer = customer;
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusDays(10);
    }

    public void setCar(Car car){
        RentalUtil.validateCar(car);
        car.setStatus(CarStatus.RESERVED);
        this.car = car;

    }

    public Car getCar(){
        return car;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public Customer getCustomer(){
        return customer;
    }

    public void setStartDate(String startDate){
        RentalUtil.ValidateDateFormat(startDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.startDate = LocalDate.parse(startDate, formatter);
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public void setEndDate(String endDate){
        RentalUtil.ValidateDateFormat(endDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.endDate = LocalDate.parse(endDate, formatter);
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public static Rental createRental(Car car, Customer customer, String startDate, String endDate){
        return new Rental(car, customer, startDate, endDate);
    }

    public static Rental createRental(Car car, Customer customer){
        return new Rental(car, customer);
    }

    @Override
    public String toString(){
        return String.format("Rental[Car=%s, Customer=%s, startDate=%s, endDate=%s]",
             car, customer, startDate, endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rental rental = (Rental) o;
        return  Objects.equals(car, rental.car) &&
                Objects.equals(customer, rental.customer) &&
                Objects.equals(startDate, rental.startDate)&&
                Objects.equals(endDate, rental.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, customer, startDate, endDate);
    }




}
