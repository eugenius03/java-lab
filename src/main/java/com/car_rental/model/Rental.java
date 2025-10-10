package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;

import com.car_rental.exception.InvalidDataException;
import com.car_rental.util.RentalUtil;

public class Rental implements Comparable<Rental> {
    private int id;
    private Car car;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;

    public Rental(){

    }

    public Rental(int id, Car car, Customer customer, String startDate, String endDate){
        setId(id);
        setCar(car);
        this.customer = customer;
        setStartDate(startDate);
        setEndDate(endDate);
        if(this.endDate.isBefore(this.startDate)){
            throw new InvalidDataException("End date cannot be before start date");
        }
    }

    public Rental(int id, Car car, Customer customer){
        setId(id);
        setCar(car);
        this.customer = customer;
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusDays(10);
    }

    @Override
    public int compareTo(Rental other){
        return Long.compare(
            ChronoUnit.DAYS.between(getStartDate(), getEndDate()),
            ChronoUnit.DAYS.between(other.getStartDate(), other.getEndDate())
        );
    }

    public static Comparator<Rental> byStartDate(){
        return Comparator.comparing(Rental::getStartDate);
    }

    public static Comparator<Rental> byEndDate(){
        return Comparator.comparing(Rental::getEndDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        RentalUtil.ValidateId(id);
        this.id = id;
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

    public static Rental createRental(int id, Car car, Customer customer, String startDate, String endDate){
        return new Rental(id, car, customer, startDate, endDate);
    }

    public static Rental createRental(int id, Car car, Customer customer){
        return new Rental(id, car, customer);
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
