package com.car_rental.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;

import com.car_rental.exception.InvalidDataException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Rental implements Comparable<Rental> {

    @NotBlank(message = "Rental ID cannot be null or blank")
    @JsonProperty("rental_id")
    private String id;

    @NotNull(message = "Car cannot be null")
    private Car car;

    @NotNull(message = "Customer cannot be null")
    private Customer customer;

    @NotNull(message = "Start date cannot be null or blank")
    @JsonProperty("start_date")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null or blank")
    @JsonProperty("end_date")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    public Rental(){

    }

    @JsonCreator
    public Rental(
        @JsonProperty("rental_id") String id,
        @JsonProperty("car") Car car,
        @JsonProperty("customer") Customer customer,
        @JsonProperty("start_date") String startDate,
        @JsonProperty("end_date") String endDate){
        setId(id);
        setCar(car);
        this.customer = customer;
        setStartDate(startDate);
        setEndDate(endDate);
        if(this.endDate.isBefore(this.startDate)){
            throw new InvalidDataException("End date cannot be before start date");
        }
    }

    public Rental(String id, Car car, Customer customer){
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public void setCar(Car car){
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.startDate = LocalDate.parse(startDate, formatter);
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public void setEndDate(String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.endDate = LocalDate.parse(endDate, formatter);
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public static Rental createRental(String id, Car car, Customer customer, String startDate, String endDate){
        return new Rental(id, car, customer, startDate, endDate);
    }

    public static Rental createRental(String id, Car car, Customer customer){
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
